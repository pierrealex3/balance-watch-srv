package org.pa.balance.service;

import org.pa.balance.model.TransactionEntity;

import java.time.LocalDate;

@DateTranslator
public class GeneralDates {

    @IndToAggr
    public LocalDate individualPartsToAggregated(TransactionEntity t) {
        return LocalDate.of(t.getYear(), t.getMonth(), t.getDay());
    }
}
