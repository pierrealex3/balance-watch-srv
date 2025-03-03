package org.pa.balance.account;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.pa.balance.client.model.Account;

@Mapper
public interface AccountMapper
{
    @Mapping(target="description", source="desc")
    AccountEntity fromDtoToEntity(Account dto);

    @Mapping(target="desc", source="description")
    Account fromEntityToDto(AccountEntity entity);

    @Mapping(target="id", ignore=true)
    void fromDetachedToManaged(AccountEntity detached, @MappingTarget AccountEntity target);

}
