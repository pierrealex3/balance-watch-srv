package org.pa.balance.transaction.mapper;

import org.pa.balance.service.DateTranslator;
import org.pa.balance.service.IndToAggr;
import org.pa.balance.transaction.entity.TransactionEntity;

import java.time.LocalDate;

@DateTranslator
public class GeneralDates {

    @IndToAggr
    public LocalDate individualPartsToAggregated(TransactionEntity t) {
        return LocalDate.of(t.getYear(), t.getMonth(), t.getDay());
    }
}
