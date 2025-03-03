package org.pa.balance.transactiont.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.pa.balance.client.model.TT;
import org.pa.balance.transactiont.entity.SpanEntity;
import org.pa.balance.transactiont.entity.TransactionTemplateEntity;

@Mapper
public interface TTMapper {

    // PUT

    @Mapping(target = "tt_id", ignore = true)
    @Mapping(target = "ttGroup", ignore = true) // required because this field cannot be modified via the DTO
    @Mapping(target = "spanList", ignore = true)
    void updateManagedWithDetached(TransactionTemplateEntity detached, @MappingTarget TransactionTemplateEntity managed);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transactionTemplate", ignore = true)
    void updateManagedSpanWithDetachedSpan(SpanEntity detached, @MappingTarget SpanEntity managed);

    // POST

    @Mapping(target = "tt_id", ignore = true)
    @Mapping(target = "acctId", source = "account")
    @Mapping(target = "acctIdConn", source = "accountConnection")
    @Mapping(target = "spanList", source = "spans")
    TransactionTemplateEntity fromDtoToEntity(TT d);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transactionTemplate", ignore = true)
    SpanEntity fromSpanDtoToEntity(org.pa.balance.client.model.Span d);

    // GET

    @Mapping(target = "account", source = "acctId")
    @Mapping(target= "accountConnection", source = "acctIdConn")
    @Mapping(target = "spans", source = "spanList")
    TT fromEntityToDto(TransactionTemplateEntity te);

}
