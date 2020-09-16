package org.pa.balance.account;

import org.pa.balance.account.repository.AccountCrudRepo;
import org.pa.balance.error.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class AccountDao
{
    @Autowired
    AccountCrudRepo crudRepo;

    @Autowired
    UserAccountRightsRepo userAccountRightsRepo;

    @Transactional
    public Long addAccount(AccountEntity ae, Long userId)
    {
        AccountEntity aee = crudRepo.save(ae);

        UserAccountRightsEntity uare = new UserAccountRightsEntity();
        uare.setId(new UserAccountRightsEntityId(userId, aee.getId()));
        uare.setRightPattern(1);    // TODO - this is write permission - needs to be changed to a bitmask
        userAccountRightsRepo.save(uare);

        return aee.getId();
    }

    @Transactional
    public List<AccountEntity> getAllAccessibleAccounts(Long userId) {

        List<Long> accountIdList = userAccountRightsRepo.findByIdUserId(userId).map( uare -> uare.getId().getAccountId() ).collect(Collectors.toList());
        if (accountIdList.isEmpty()) {
            throw new EntityNotFoundException(String.format("Cannot find any accounts associated with userId : %d", userId));
        }
        return StreamSupport.stream(crudRepo.findAllById(accountIdList).spliterator(), false).collect(Collectors.toList());
    }
}
