package org.pa.balance.account.repository;

import org.mapstruct.factory.Mappers;
import org.pa.balance.account.*;
import org.pa.balance.client.model.Account;
import org.pa.balance.client.model.AccountWrapper;
import org.pa.balance.user.UserDao;
import org.pa.balance.user.UserEntity;
import org.pa.balance.user.info.UserInfoProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountDelegate
{

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserInfoProxy userInfoProxy;

    public Long addAccount(String userId, Account body)
    {
        UserEntity ue = userDao.getUser(userId);    // will throw if not found
        AccountMapper m = Mappers.getMapper(AccountMapper.class);
        AccountEntity ae = m.fromDtoToEntity(body);

        final String authenticatedUser = userInfoProxy.getAuthenticatedUser();
        if (!userId.equals(authenticatedUser))
            throw new AddAccountForbiddenException(String.format("The user creating the account for : %s must be : %s", userId, authenticatedUser));

        UserAccountRightsPattern rightsPattern = new UserAccountRightsPattern.UserAccountRightsPatternBuilder().addVip().build();

        return accountDao.addAccount(ae, ue, rightsPattern);
    }

    public List<AccountWrapper> getAccounts(String userId)
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

    /**
     * TODO add required role security to access this method
     * @param userId
     * @return
     */
    public List<AccountWrapper> getRelatedAccounts(String userId)
    {
        userDao.getUser(userId);    // will throw if not found
        AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

        Optional<String> targetGroup = userInfoProxy.getAuthenticatedUserTargetGroup();
        List<String> userIds = Arrays.asList(userId);
        if (targetGroup.isPresent()) {
            userIds = userInfoProxy.getUsersForGroup(targetGroup.get());
        }

        return accountDao.getAllAccessibleAccounts(userIds).stream().map( ae -> {
            var aw = new AccountWrapper();
            aw.setId(ae.getId());
            aw.setData( mapper.fromEntityToDto(ae));
            return aw;
        }).collect(Collectors.toList());

    }

    public AccountWrapper getAccount(Long accountId)
    {
        AccountMapper m = Mappers.getMapper(AccountMapper.class);
        AccountEntity ae = accountDao.getAccount(accountId);
        AccountWrapper aw = new AccountWrapper();
        aw.setId(ae.getId());
        aw.setData(m.fromEntityToDto(ae));
        return aw;
    }

    public void updateAccount(Long accountId, Account body)
    {
        AccountMapper m = Mappers.getMapper(AccountMapper.class);
        AccountEntity detached = m.fromDtoToEntity(body);
        accountDao.updateAccount(accountId, detached);
    }
}
