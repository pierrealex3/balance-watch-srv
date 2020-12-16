package org.pa.balance.transactiont;

import org.mapstruct.factory.Mappers;
import org.pa.balance.algo.FrequencyGeneratorLocalizer;
import org.pa.balance.client.model.Span;
import org.pa.balance.client.model.TT;
import org.pa.balance.client.model.TTWrapperRes;
import org.pa.balance.client.model.TransactionWrapper;
import org.pa.balance.transaction.TransactionDelegate;
import org.pa.balance.transactiont.entity.TransactionTemplateEntity;
import org.pa.balance.transactiont.mapper.TTMapper;
import org.pa.balance.transactiont.repository.TTRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class TTDelegate {

    @Autowired
    TTRepo transactionTemplateDao;

    @Autowired
    FrequencyGeneratorLocalizer frequencyGeneratorLocalizer;

    @Autowired
    TransactionDelegate transactionDelegate;

    /**
     * At this point, the validation has been done for a minimum number of 1 Frequency items in the dto - look at generated pojo for JSR-380 annotation.
     * @param body
     * @param ttGroupId
     * @return
     */
    public Long addTransactionTemplate(TT body, Long ttGroupId) {
        List<Span> spanList = body.getSpans();
        if (spanList == null || spanList.isEmpty()) {   // first idea was to put this default in the swagger, but it does not seem an option.
            Span s = new Span();
            s.setStartDate(LocalDate.now());
            s.setEndDate(LocalDate.of(9999, Month.DECEMBER, 31));
            spanList = new ArrayList<>();
            spanList.add(s);
            body.setSpans(spanList);
        }

        TTMapper ttMapper = Mappers.getMapper(TTMapper.class);
        TransactionTemplateEntity tte = ttMapper.fromDtoToEntity(body);
        Long id = transactionTemplateDao.add(tte, ttGroupId);
        return id;
    }

    public List<TTWrapperRes> getTransactionTemplates(Long account) {
        List<TransactionTemplateEntity> teList = transactionTemplateDao.getTransactionTemplates(account);
        return mapEntityListToDtoWrapper(teList);
    }

    public List<TTWrapperRes> findAllTransactionTemplatesForGroup(Long groupId)
    {
        List<TransactionTemplateEntity> teList = transactionTemplateDao.getTransactionTemplatesForGroup(groupId);
        return mapEntityListToDtoWrapper(teList);
    }

    List<TTWrapperRes> mapEntityListToDtoWrapper(List<TransactionTemplateEntity> teList) {
        List<TTWrapperRes> ttWrapperResList = new ArrayList<>();
        TTMapper ttMapper = Mappers.getMapper(TTMapper.class);

        teList.forEach( te -> {
            TT ttRes = ttMapper.fromEntityToDto(te);
            TTWrapperRes ttWrapperRes = new TTWrapperRes();
            ttWrapperRes.setId(te.getTt_id());
            ttWrapperRes.setData(ttRes);
            ttWrapperResList.add(ttWrapperRes);
        });

        return ttWrapperResList;
    }

    public void updateTransactionTemplate(Long id, TT data) {
        TTMapper ttMapper = Mappers.getMapper(TTMapper.class);
        TransactionTemplateEntity tte = ttMapper.fromDtoToEntity(data);
        transactionTemplateDao.update(id, tte);
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
        return transactionDelegate.generateTransactions(genTransactionDateList, tte);
    }
}
