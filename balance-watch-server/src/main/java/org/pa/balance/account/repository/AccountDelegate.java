package org.pa.balance.account.repository;

import org.mapstruct.factory.Mappers;
import org.pa.balance.account.*;
import org.pa.balance.client.model.Account;
import org.pa.balance.client.model.AccountWrapper;
import org.pa.balance.client.model.UserAccountRights;
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

    /**
     * Wrap accounts returned by the dao layer.
     * @param accountEntityList
     * @return
     */
    List<AccountWrapper> wrapAccounts(List<AccountEntity> accountEntityList) {
        AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

        return accountEntityList.stream().map(ae -> {
            var aw = new AccountWrapper();
            aw.setId(ae.getId());
            aw.setData( mapper.fromEntityToDto(ae));
            return aw;
        }).collect(Collectors.toList());
    }

    /**
     * Get accounts available to transfer to.
     * @param userId
     * @return
     */
    public List<AccountWrapper> getAccountsForTransfer(String userId) {
        return wrapAccounts(accountDao.getAllAccessibleAccountsForSpecificRight(userId, UserAccountRightsPattern::isTransfer));
    }

    /**
     * Get accounts available for read.
     * @param userId
     * @return
     */
    public List<AccountWrapper> getAccountsForRead(String userId) {
        return wrapAccounts(accountDao.getAllAccessibleAccountsForSpecificRight(userId, UserAccountRightsPattern::isRead));
    }

    /**
     * Get all accounts for which an access has been made available.
     * @param userId
     * @return
     */
    public List<AccountWrapper> getAllAccessibleAccounts(String userId) {
        return wrapAccounts(accountDao.getAllAccessibleAccounts(userId));
    }

    /**
     * Returns all accounts associated to the group (i.e. MDS_* group in KeyCloak database) of the authenticated user.
     * @return
     */
    public List<AccountWrapper> getRelatedAccounts()
    {
        AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

        Optional<String> targetGroup = userInfoProxy.getAuthenticatedUserTargetGroup();
        List<String> userIds = Arrays.asList(userInfoProxy.getAuthenticatedUser());
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

    public UserAccountRightsPattern getUserAccountRights(Long accountId) {
        return getUserAccountRights(accountId, userInfoProxy.getAuthenticatedUser());
    }

    public UserAccountRightsPattern getUserAccountRights(Long accountId, String userId) {
        return Optional.ofNullable(accountDao.getUserAccountRightsPattern(userId , accountId)).map(UserAccountRightsPattern::from).orElseThrow( () -> new UnrelatedAccountException(String.format("User : %s has no rights on account : %d", userInfoProxy.getAuthenticatedUser(), accountId)) );
    }

    public UserAccountRights fetchUserAccountRights(Long accountId, String userId) {
        UserAccountRightsMapper mapper = Mappers.getMapper(UserAccountRightsMapper.class);
        return mapper.fromRightPatternToDto(getUserAccountRights(accountId, userId));
    }

    public void createUserAccountRights(String userId, Long accountId, UserAccountRights body) {
        UserAccountRightsEntity uare = new UserAccountRightsEntity();
        uare.setRightPattern(UserAccountRightsMapper.fromDtoToRightPattern(body).getRightsPattern());

        UserAccountRightsEntityId uareId = new UserAccountRightsEntityId(userDao.getUser(userId), accountDao.getAccount(accountId));
        uare.setId(uareId);
        accountDao.saveUserAccountRights(uare);
    }

    public void updateUserAccountRights(String userId, Long accountId, UserAccountRights body) {
        UserAccountRightsEntity uare = accountDao.getUserAccountRights(userId, accountId);
        uare.setRightPattern(UserAccountRightsMapper.fromDtoToRightPattern(body).getRightsPattern());
        accountDao.saveUserAccountRights(uare);
    }
}
