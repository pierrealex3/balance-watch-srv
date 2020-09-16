package org.pa.balance.account.repository;

import org.mapstruct.factory.Mappers;
import org.pa.balance.account.*;
import org.pa.balance.client.model.Account;
import org.pa.balance.client.model.AccountWrapper;
import org.pa.balance.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountDelegate
{

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private UserDao userDao;

    public Long addAccount(Long userId, Account body)
    {
        userDao.getUser(userId);    // will throw if not found
        AccountMapper m = Mappers.getMapper(AccountMapper.class);
        AccountEntity ae = m.fromDtoToEntity(body);

        return accountDao.addAccount(ae, userId);
    }

    public List<AccountWrapper> getAccounts(Long userId)
    {
        userDao.getUser(userId);    // will throw if not found
        AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

        return accountDao.getAllAccessibleAccounts(userId).stream().map( ae -> {
            var aw = new AccountWrapper();
            aw.setId(ae.getId());
            aw.setData( mapper.fromEntityToDto(ae));
            return aw;
        }).collect(Collectors.toList());
    }
}
