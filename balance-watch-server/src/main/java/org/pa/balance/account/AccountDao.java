package org.pa.balance.account;

import org.mapstruct.factory.Mappers;
import org.pa.balance.account.repository.AccountCrudRepo;
import org.pa.balance.error.EntityNotFoundException;
import org.pa.balance.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AccountDao
{
    @Autowired
    AccountCrudRepo crudRepo;

    @Autowired
    UserAccountRightsRepo userAccountRightsRepo;

    @Transactional
    public Long addAccount(AccountEntity ae, UserEntity ue)
    {
        AccountEntity aee = crudRepo.save(ae);

        UserAccountRightsEntity uare = new UserAccountRightsEntity();
        uare.setId(new UserAccountRightsEntityId());
        uare.getId().setAccount(ae);
        uare.getId().setUser(ue);
        uare.setRightPattern(1);    // TODO - this is write permission - needs to be changed to a bitmask
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
    public List<AccountEntity> getAllAccessibleAccounts(List<String> userIds) {
        List<AccountEntity> accountList = userAccountRightsRepo.findAllByIdUserIdIn(userIds).map( uare -> uare.getId().getAccount() ).collect(Collectors.toList());
        if (accountList.isEmpty()) {
            throw new EntityNotFoundException(String.format("Cannot find any accounts associated with userIds : %s", "CHANGETHAT"));
        }
        return accountList;
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
}
