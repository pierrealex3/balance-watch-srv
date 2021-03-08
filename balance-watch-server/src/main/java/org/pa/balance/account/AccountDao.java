package org.pa.balance.account;

import org.mapstruct.factory.Mappers;
import org.pa.balance.account.repository.AccountCrudRepo;
import org.pa.balance.error.EntityNotFoundException;
import org.pa.balance.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class AccountDao
{
    @Autowired
    AccountCrudRepo crudRepo;

    @Autowired
    UserAccountRightsRepo userAccountRightsRepo;

    @Transactional
    public Long addAccount(AccountEntity ae, UserEntity ue, UserAccountRightsPattern rightsPattern)
    {
        AccountEntity aee = crudRepo.save(ae);

        UserAccountRightsEntity uare = new UserAccountRightsEntity();
        uare.setId(new UserAccountRightsEntityId());
        uare.getId().setAccount(ae);
        uare.getId().setUser(ue);
        uare.setRightPattern(rightsPattern.getRightsPattern());
        userAccountRightsRepo.save(uare);

        return aee.getId();
    }

    @Transactional
    public List<AccountEntity> getAllAccessibleAccounts(String userId) {
        List<AccountEntity> accountList = userAccountRightsRepo.findByIdUserId(userId).map( uare -> uare.getId().getAccount() ).collect(Collectors.toList());
        if (accountList.isEmpty()) {
            throw new EntityNotFoundException(String.format("Cannot find any accounts associated with userId : %s", userId));
        }
        return accountList;
    }

    @Transactional
    public Set<AccountEntity> getAllAccessibleAccounts(List<String> userIds) {
        Set<AccountEntity> accountSet = userAccountRightsRepo.findAllByIdUserIdIn(userIds).map( uare -> uare.getId().getAccount() ).collect(Collectors.toSet());
        if (accountSet.isEmpty()) {
            throw new EntityNotFoundException(String.format("Cannot find any accounts associated with userIds : %s", userIds));
        }
        return accountSet;
    }

    @Transactional
    public Integer getUserAccountRightsPattern(String userId, Long accountId) {
        UserAccountRightsEntity uare = userAccountRightsRepo.findByIdUserIdAndIdAccountId(userId, accountId);
        return uare == null ? null : uare.getRightPattern();
    }

    @Transactional
    public UserAccountRightsEntity getUserAccountRights(String userId, Long accountId) {
        return userAccountRightsRepo.findByIdUserIdAndIdAccountId(userId, accountId);
    }

    @Transactional
    public AccountEntity getAccount(Long accountId)
    {
        return crudRepo.findById(accountId).orElseThrow(() -> new EntityNotFoundException(String.format("Cannot find any account with id: %d", accountId)));
    }

    @Transactional
    public void updateAccount(Long accountId, AccountEntity detached)
    {
        AccountEntity managed = getAccount(accountId);
        AccountMapper m = Mappers.getMapper(AccountMapper.class);
        m.fromDetachedToManaged(detached, managed);
    }

    @Transactional
    public void saveUserAccountRights(UserAccountRightsEntity uare) {
        userAccountRightsRepo.save(uare);
    }
}
