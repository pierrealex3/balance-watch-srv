package org.pa.balance.user;

import org.mapstruct.factory.Mappers;
import org.pa.balance.error.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDao
{
    @Autowired
    UserCrudRepo crudRepo;

    @Transactional
    public Long addUser(UserEntity ue) {
        return crudRepo.save(ue).getId();
    }

    @Transactional
    public UserEntity getUser(Long userId)
    {
        return crudRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException(String.format("Cannot find any user with id: %d", userId)));
    }

    @Transactional
    public void updateUser(Long userId, UserEntity detached)
    {
        UserMapper m = Mappers.getMapper(UserMapper.class);
        m.fromDetachedToManaged(detached, getUser(userId));
    }
}
