package org.pa.balance.transaction.repository;

import org.mapstruct.factory.Mappers;
import org.pa.balance.error.EntityNotFoundException;
import org.pa.balance.transaction.entity.TransactionEntity;
import org.pa.balance.transaction.mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    public Long addTransaction(TransactionEntity t) {
        TransactionEntity te = crudRepo.save(t);
        return te.getId();
    }

    @Transactional
    public Long updateTransaction(TransactionEntity t, Long id) {

        TransactionEntity mte = crudRepo.findById(id).orElseThrow( () -> new EntityNotFoundException(String.format("No transaction found for : id=%d", id)) );

        TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);

        mapper.updateManagedWithDetached(t, mte);
        TransactionEntity ute = crudRepo.save(mte);
        return ute.getId();
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
}
