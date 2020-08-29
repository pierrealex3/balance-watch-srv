package org.pa.balance.transaction.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.pa.balance.client.model.Transaction;
import org.pa.balance.service.DateTranslator;
import org.pa.balance.service.IndToAggr;
import org.pa.balance.transaction.entity.TransactionEntity;

@Mapper( uses = GeneralDates.class )
public interface TransactionMapper {

    void updateManagedWithDetached(TransactionEntity detached, @MappingTarget TransactionEntity managed);

    @Mapping(target = "date", source="t", qualifiedBy = {DateTranslator.class, IndToAggr.class})
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "account", source = "acctId")
    @Mapping(target = "accountConnection", source = "acctIdConn")
    Transaction fromEntityToDto(TransactionEntity t);

    @Mapping(target = "year", source = "date.year")
    @Mapping(target = "month", source = "date.monthValue")
    @Mapping(target = "day", source = "date.dayOfMonth")
    @Mapping(target = "hours", source = "date.hour")
    @Mapping(target = "minutes", source = "date.minute")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "acctId", source = "account")
    @Mapping(target = "acctIdConn", source = "accountConnection")
    TransactionEntity fromDtoToEntity(Transaction t);
}
