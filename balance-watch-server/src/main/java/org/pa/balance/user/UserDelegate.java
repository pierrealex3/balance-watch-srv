package org.pa.balance.user;

import org.mapstruct.factory.Mappers;
import org.pa.balance.client.model.User;
import org.pa.balance.user.info.UserInfoProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDelegate
{
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserInfoProxy userInfoProxy;

    public String addUser(User u) {
        UserMapper m = Mappers.getMapper(UserMapper.class);
        UserEntity ue = m.fromDtoToEntity(u);
        return userDao.addUser(ue);
    }

    public List<User> getAllUsersUnderGroupRestriction() {
        List<User> users;
        Optional<String> tg = userInfoProxy.getAuthenticatedUserTargetGroup();
        if (tg.isPresent()) {
            users = userInfoProxy.getUsersForGroup(tg.get()).stream().map( u -> new User().id(u) ).collect(Collectors.toList());
        } else {
            var u = new User().id(userInfoProxy.getAuthenticatedUser());
            users = Arrays.asList(u);
        }
        return users;
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
