package org.pa.balance.account.repository;

import org.pa.balance.account.AccountEntity;
import org.springframework.data.repository.CrudRepository;

public interface AccountCrudRepo extends CrudRepository<AccountEntity, Long>
{
}
