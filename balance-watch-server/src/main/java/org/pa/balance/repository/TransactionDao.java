package org.pa.balance.repository;

import org.mapstruct.factory.Mappers;
import org.pa.balance.error.EntityNotFoundException;
import org.pa.balance.model.TransactionEntity;
import org.pa.balance.service.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class TransactionDao {

    @Autowired
    private TransactionCrudRepo crudRepo;

    @Transactional
    public List<TransactionEntity> getTransactions(Integer year, Integer month, String account) {

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

        Optional<TransactionEntity> te = crudRepo.findById(id);
        if (te.isEmpty())
            throw new EntityNotFoundException(String.format("No transaction found for : id=%d", id));

        TransactionEntity mte = te.get();
        TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);

        mapper.updateManagedWithDetached(t, mte);
        TransactionEntity ute = crudRepo.save(mte);
        return ute.getId();
    }




}
