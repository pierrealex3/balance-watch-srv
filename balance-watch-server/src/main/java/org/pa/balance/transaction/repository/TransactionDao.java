package org.pa.balance.transaction.repository;

import org.mapstruct.factory.Mappers;
import org.pa.balance.error.EntityNotFoundException;
import org.pa.balance.transaction.UpdateTransactionConcurrentException;
import org.pa.balance.transaction.entity.TransactionEntity;
import org.pa.balance.transaction.mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

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
        TransactionEntity mte = crudRepo.findById(id).orElseThrow( () -> new EntityNotFoundException(String.format("No transaction found for : id=%d", id)) );

        if (mte.getDateModified().toInstant().toEpochMilli() != lastModifiedEpoch)
            throw new UpdateTransactionConcurrentException();

        TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);

        long lastUpdated = setDateNowUtc(t);
        mapper.updateManagedWithDetached(t, mte);
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
        Arrays.stream(te).forEach( (tee) -> tee.setDateModified(zdt) );
        return zdt.toInstant().toEpochMilli();
    }
}
