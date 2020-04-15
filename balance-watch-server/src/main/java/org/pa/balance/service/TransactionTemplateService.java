package org.pa.balance.service;


import org.pa.balance.model.Span;
import org.pa.balance.model.SpanRepo;
import org.pa.balance.transactiont.entity.TransactionTemplateEntity;
import org.pa.balance.model.TransactionTemplateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TransactionTemplateService {

    @Autowired
    TransactionTemplateRepo transactionTemplateRepo;

    @Autowired
    SpanRepo spanRepo;

    public Iterable<TransactionTemplateEntity> fetchAll() {
        return transactionTemplateRepo.findAll();
    }

    public void addDefaultSpan(Long ttid) {

        TransactionTemplateEntity tt = transactionTemplateRepo.findById(ttid).get();

        Span s = new Span();
        s.setStartDate(LocalDate.now());
        s.setTransactionTemplate(tt);

        tt.getSpanList().add(s);
        transactionTemplateRepo.save(tt);

    }
}
