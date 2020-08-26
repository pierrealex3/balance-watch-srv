package org.pa.balance.transaction.mapper;

import org.pa.balance.service.DateTranslator;
import org.pa.balance.service.IndToAggr;
import org.pa.balance.transaction.entity.TransactionEntity;

import java.time.LocalDateTime;

@DateTranslator
public class GeneralDates {

    @IndToAggr
    public LocalDateTime individualPartsToAggregated(TransactionEntity t) {
        return LocalDateTime.of(t.getYear(), t.getMonth(), t.getDay(), t.getHours(), t.getMinutes());
    }
}
