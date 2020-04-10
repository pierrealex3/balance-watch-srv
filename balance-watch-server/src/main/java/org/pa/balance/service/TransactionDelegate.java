package org.pa.balance.service;

import org.mapstruct.factory.Mappers;
import org.pa.balance.client.model.Transaction;
import org.pa.balance.model.TransactionEntity;
import org.pa.balance.repository.TransactionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionDelegate {

    @Autowired
    TransactionDao transactionDao;

    public List<Transaction> getTransactions(Integer year, Integer month, String account) {
        List<TransactionEntity> transactionEntities = transactionDao.getTransactions(year, month, account);

        TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);

        List<Transaction> transactions = new ArrayList<>();
        transactionEntities.forEach( te -> transactions.add(mapper.fromEntityToDto(te)) );

        return transactions;
    }

    public Long addTransaction(Transaction t) {
        TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);
        TransactionEntity te = mapper.fromDtoToEntity(t);
        return transactionDao.addTransaction(te);
    }

    public Long updateTransaction(Transaction t, Long id) {
        TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);
        TransactionEntity te = mapper.fromDtoToEntity(t);
        te.setId(id);
        return transactionDao.updateTransaction(te, id);
    }
}
