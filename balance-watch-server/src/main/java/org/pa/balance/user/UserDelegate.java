package org.pa.balance.user;

import org.mapstruct.factory.Mappers;
import org.pa.balance.client.model.User;
import org.pa.balance.client.model.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDelegate
{
    @Autowired
    private UserDao userDao;

    public Long addUser(User u) {
        UserMapper m = Mappers.getMapper(UserMapper.class);
        UserEntity ue = m.fromDtoToEntity(u);
        return userDao.addUser(ue);
    }

    public UserWrapper getUser(Long userId)
    {
        UserEntity ue = userDao.getUser(userId);
        UserMapper m = Mappers.getMapper(UserMapper.class);
        User u = m.fromEntityToDto(ue);
        UserWrapper uw = new UserWrapper();
        uw.setData(u);
        uw.setId(ue.getId());

        return uw;
    }

    public void updateUser(Long userId, User body)
    {
        UserMapper m = Mappers.getMapper(UserMapper.class);
        UserEntity detached = m.fromDtoToEntity(body);
        userDao.updateUser(userId, detached);
    }


}
