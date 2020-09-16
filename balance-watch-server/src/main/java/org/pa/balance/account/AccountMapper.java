package org.pa.balance.account;

import org.mapstruct.Mapper;
import org.pa.balance.client.model.Account;

@Mapper
public interface AccountMapper
{
    AccountEntity fromDtoToEntity(Account dto);

    Account fromEntityToDto(AccountEntity entity);

}
