package org.pa.balance.transaction;

import org.mapstruct.factory.Mappers;
import org.pa.balance.algo.entity.FrequencyStaticEntity;
import org.pa.balance.client.model.Transaction;
import org.pa.balance.client.model.TransactionWrapper;
import org.pa.balance.frequency.entity.FrequencyConfigEntity;
import org.pa.balance.algo.AbstractDateTranslator;
import org.pa.balance.transaction.entity.TransactionEntity;
import org.pa.balance.transaction.mapper.TransactionMapper;
import org.pa.balance.transaction.repository.TransactionDao;
import org.pa.balance.transactiont.entity.TransactionTemplateEntity;
import org.pa.balance.transactiont.repository.TTRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TransactionDelegate {

    @Autowired
    TransactionDao transactionDao;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    TTRepo transactionTemplateDao;

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
     * Return the list of generated Transactions, based on a Transaction Template.
     * Note: the previous transaction date may be required for some transaction date generation algorithms.
     * @param yearMonth
     * @param ttId
     * @param refTransactionDate
     * @return
     */
    public List<Transaction> generateTransactions(YearMonth yearMonth, Long ttId, LocalDate refTransactionDate) {

        List<Transaction> genTransactionList = new ArrayList<>();

        TransactionTemplateEntity tte = transactionTemplateDao.findById(ttId);
        Set<FrequencyConfigEntity> frequencyConfigEntitySet = tte.getFrequencyList();

        List<LocalDate> genTransactionDateList = new ArrayList<>(); // the list of all the transaction dates generated based on the transaction template

        frequencyConfigEntitySet.forEach( frequencyConfigEntity -> {
            FrequencyStaticEntity frequencyStaticEntity = frequencyConfigEntity.getAlgoTag();
            String algoSpec = frequencyConfigEntity.getAlgoSpec();

            Boolean refDateRequired = frequencyStaticEntity.getRefDateRequired();
            String algoTag = frequencyStaticEntity.getAlgoTag();

            AbstractDateTranslator dateTranslatorAlgo = applicationContext.getBean(algoTag, AbstractDateTranslator.class);  // TODO make sure a 500 is thrown if bean not found

            List<LocalDate> genDateList = dateTranslatorAlgo.fetch(yearMonth, algoSpec, refTransactionDate);
            genTransactionDateList.addAll(genDateList);
        });

        genTransactionDateList.forEach( d -> {
            Transaction t = new Transaction();
            t.setDate(d);
            t.setType(tte.getType());
            t.setAmount(tte.getAmount());
            t.setWay(Transaction.WayEnum.fromValue(tte.getWay().toString()));
            // t.setNote(tte.getNote());    // TODO add this to tt - note should be added, but not note extra, which is more aimed at a specific occurence of a transaction
            t.setAccount(tte.getAcctId());  // TODO should add the connAccountId as well

            genTransactionList.add(t);
        });

        return genTransactionList;
    }
}
