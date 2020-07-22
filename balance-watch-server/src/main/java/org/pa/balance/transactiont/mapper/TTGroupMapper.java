package org.pa.balance.transactiont.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.pa.balance.client.model.TTGroup;
import org.pa.balance.transactiont.entity.TransactionTemplateGroupEntity;

@Mapper
public interface TTGroupMapper
{
    @Mapping(target = "transactionTemplateEntityList", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "acctId", ignore = true)
    void fromDetachedToManaged(TransactionTemplateGroupEntity detached, @MappingTarget TransactionTemplateGroupEntity managed);

    @Mapping(target = "account", source = "acctId")
    TTGroup fromEntityToDto(TransactionTemplateGroupEntity entity);

    @Mapping(target = "acctId", source = "account")
    TransactionTemplateGroupEntity fromDtoToEntity(TTGroup dto);

    @Mapping(target = "acctId", ignore = true)
    TransactionTemplateGroupEntity updateFromDtoToEntity(TTGroup dto);

}
