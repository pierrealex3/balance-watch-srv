package org.pa.balance.user;

import org.mapstruct.factory.Mappers;
import org.pa.balance.client.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserDelegate
{
    @Autowired
    private UserDao userDao;

    public String addUser(User u) {
        UserMapper m = Mappers.getMapper(UserMapper.class);
        UserEntity ue = m.fromDtoToEntity(u);
        return userDao.addUser(ue);
    }

    @Cacheable(value="users")
    public User getUser(String userId)
    {
        UserEntity ue = userDao.getUser(userId);
        UserMapper m = Mappers.getMapper(UserMapper.class);
        return m.fromEntityToDto(ue);
    }

    @CacheEvict(value="users", allEntries = true)
    public void clearUserCache() { /* Spring Framework driven feature */ }

}
