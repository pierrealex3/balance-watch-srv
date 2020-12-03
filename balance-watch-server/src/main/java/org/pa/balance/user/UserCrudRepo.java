package org.pa.balance.user;

import org.springframework.data.repository.CrudRepository;

public interface UserCrudRepo extends CrudRepository<UserEntity, String>
{
}
