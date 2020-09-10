package org.pa.balance.transaction;

import org.mapstruct.factory.Mappers;
import org.pa.balance.algo.FrequencyGeneratorLocalizer;
import org.pa.balance.client.model.Transaction;
import org.pa.balance.client.model.TransactionWrapper;
import org.pa.balance.transaction.entity.TransactionEntity;
import org.pa.balance.transaction.mapper.TransactionMapper;
import org.pa.balance.transaction.repository.TransactionDao;
import org.pa.balance.transactiont.entity.TransactionTemplateEntity;
import org.pa.balance.transactiont.repository.TTRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionDelegate {

    @Autowired
    TransactionDao transactionDao;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    TTRepo transactionTemplateDao;

    @Autowired
    FrequencyGeneratorLocalizer frequencyGeneratorLocalizer;

    public List<TransactionWrapper> getTransactions(Integer year, Integer month, String account) {
        List<TransactionEntity> transactionEntities = transactionDao.getTransactions(year, month, account);

        TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);

        List<TransactionWrapper> transactions = new ArrayList<>();
        transactionEntities.forEach( te -> {
            TransactionWrapper tw = new TransactionWrapper();
            tw.setId(te.getId());
            tw.setData(mapper.fromEntityToDto(te));
            transactions.add(tw);
        });

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

    /**
     * Return the list of generated (and persisted) Transactions, based on a Transaction Template.
     * Note: the previous transaction date may be required for some transaction date generation algorithms.
     * @param yearMonth
     * @param ttId
     * @return
     */
    public List<TransactionWrapper> generateTransactions(YearMonth yearMonth, Long ttId) {

        TransactionTemplateEntity tte = transactionTemplateDao.findById(ttId);
        String frequencyDescriptor = tte.getFrequency();
        List<LocalDateTime> genTransactionDateList = frequencyGeneratorLocalizer.localize(frequencyDescriptor).generate(frequencyDescriptor, yearMonth, tte.getSpanList());

        TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);
        return genTransactionDateList.stream().map( d -> {

            TransactionEntity te = new TransactionEntity();
            te.setYear(d.getYear());
            te.setMonth(d.getMonthValue());
            te.setDay(d.getDayOfMonth());
            te.setHours(d.getHour());
            te.setMinutes(d.getMinute());

            te.setType(tte.getType());
            te.setAmount(tte.getAmount());
            te.setWay(tte.getWay());
            te.setNote(tte.getNote());
            te.setAcctId(tte.getAcctId());  // TODO should add the connAccountId as well

            te.setTtIdGen(ttId);    // useful for tracing the origin of a transaction: tt-generated or created manually?

            Long tid = transactionDao.addTransaction(te);
            Transaction t = mapper.fromEntityToDto(te);
            TransactionWrapper tw = new TransactionWrapper();
            tw.setId(tid);
            tw.setData(t);

            return tw;
        }).collect(Collectors.toList());

    }

    public Transaction getTransaction(Long id)
    {
        TransactionEntity te = transactionDao.getTransaction(id);
        TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);
        return mapper.fromEntityToDto(te);
    }
}
