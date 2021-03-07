package org.pa.balance.account;

import org.pa.balance.account.repository.AccountDelegate;
import org.pa.balance.client.api.AccountsApi;
import org.pa.balance.client.model.Account;
import org.pa.balance.client.model.AccountWrapper;
import org.pa.balance.error.UnauthorizedUserException;
import org.pa.balance.user.info.UserInfoProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AccountController implements AccountsApi
{
    @Autowired
    AccountDelegate accountDelegate;

    @Autowired
    UserInfoProxy userInfoProxy;

    @Override
    public ResponseEntity<AccountWrapper> accountsAccountIdGet(Long accountId)
    {
        AccountWrapper a = accountDelegate.getAccount(accountId);
        return new ResponseEntity<>(a, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> accountsAccountIdPut(Long accountId, @Valid Account body)
    {
        accountDelegate.updateAccount(accountId, body);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<AccountWrapper>> accountsGet() {
        if (!userInfoProxy.isAuthenticatedUserGroupAdmin()) {
            throw new UnauthorizedUserException("Authenticated user does not have the \"group_admin\" role required to view all accounts associated to the group");
        }

        List<AccountWrapper> accountWrapperList = accountDelegate.getRelatedAccounts();
        return new ResponseEntity<>(accountWrapperList, HttpStatus.OK);
    }
}
