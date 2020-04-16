package org.pa.balance.transactiont.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.pa.balance.client.model.TTReq;
import org.pa.balance.client.model.TTRes;
import org.pa.balance.transactiont.entity.SpanEntity;
import org.pa.balance.transactiont.entity.TransactionTemplateEntity;

@Mapper
public interface TTMapper {

    @Mapping(target = "tt_id", ignore = true)
    void updateManagedWithDetached(TransactionTemplateEntity detached, @MappingTarget TransactionTemplateEntity managed);

    @Mapping(target = "tt_id", ignore = true)
    @Mapping(target = "acctId", source = "account")
    @Mapping(target = "frequencyList", ignore = true)
    TransactionTemplateEntity fromDtoToEntity(TTReq d);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transactionTemplate", ignore = true)
    SpanEntity fromSpanDtoToEntity(org.pa.balance.client.model.Span d);

    @Mapping(target = "account", source = "acctId")
    @Mapping(target= "accountConnection", ignore = true)
    @Mapping(target = "frequencies", source = "frequencyList")
    TTRes fromEntityToDto(TransactionTemplateEntity te);
}
