package org.pa.balance.transaction.repository;

import org.mapstruct.factory.Mappers;
import org.pa.balance.error.EntityNotFoundException;
import org.pa.balance.transaction.UpdateTransactionConcurrentException;
import org.pa.balance.transaction.entity.TransactionEntity;
import org.pa.balance.transaction.mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TransactionDao {

    @Autowired
    private TransactionCrudRepo crudRepo;

    @Transactional
    public List<TransactionEntity> getTransactions(Integer year, Integer month, Long account) {

        List<TransactionEntity> transactionEntityList = crudRepo.findByYearAndMonthAndAcctId(year, month, account);

        if (transactionEntityList == null || transactionEntityList.isEmpty())
            throw new EntityNotFoundException(String.format("No transaction found for : year=%d, month=%d, account=%s", year, month, account));

        return transactionEntityList;
    }

    @Transactional
    public Long addTransaction(TransactionEntity ... t) {
        setDateNowUtc(t[0]);
        TransactionEntity te = crudRepo.save(t[0]);
        if (t[1] != null)
        {   // this is where we link the 2 entities *forever*
            TransactionEntity teConn = crudRepo.save(t[1]);
            te.setIdConn(teConn.getId());
            teConn.setIdConn(te.getId());
        }
        return te.getId();  // TODO still useful to return that?
    }

    @Transactional
    public long updateTransaction(@NotNull TransactionEntity t, @NotNull Long id, @NotNull Long lastModifiedEpoch) {
        TransactionEntity tbme = crudRepo.findById(id).orElseThrow( () -> new EntityNotFoundException(String.format("No transaction found for : id=%d", id)) );

        if (tbme.getDateModified().toInstant().toEpochMilli() != lastModifiedEpoch)
            throw new UpdateTransactionConcurrentException();

        TransactionEntity tbmeConn = Optional.ofNullable(tbme.getIdConn()).map( this::getTransaction ).orElse(null);

        TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);

        long lastUpdated = setDateNowUtc(t);

        if (tbmeConn != null) {
            // lock both "Transaction" database  rows for update
            crudRepo.selectForUpdateWithConnectedTransaction(tbme.getId(), tbmeConn.getId());
            mapper.updateConnectedManagedWithDetached(t, tbmeConn);
        }

        mapper.updateManagedWithDetached(t, tbme);
        return lastUpdated;
    }

    @Transactional
    public TransactionEntity getTransaction(Long id)
    {
        return crudRepo.findById(id).orElseThrow( () -> new EntityNotFoundException(String.format("No transaction found for : id=%d", id)));
    }

    @Transactional
    public void deleteTransaction(Long id)
    {
        crudRepo.deleteById(id);
    }

    /**
     * Saves the last modification into the database for the given transaction.
     * Note: the date is saved as UTC because of either:
     * ?serverTimezone=UTC in the JDBC URI
     * spring.jpa.properties.hibernate.jdbc.time_zone=UTC // supported in MySql8
     * @param te
     * @return
     */
    long setDateNowUtc(TransactionEntity ... te) {
        var zdt = ZonedDateTime.now();
        Arrays.stream(te).filter(Objects::nonNull).forEach( (tee) -> tee.setDateModified(zdt) );
        return zdt.toInstant().toEpochMilli();
    }

    @Cacheable("transactionAccounts")
    public Long getAccountHoldingTransaction(Long transactionId)
    {
        return crudRepo.selectAccountForTransactionId(transactionId);
    }
}
