package org.pa.balance.account;

import org.pa.balance.account.repository.AccountDelegate;
import org.pa.balance.client.api.AccountsApi;
import org.pa.balance.client.model.Account;
import org.pa.balance.client.model.AccountWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AccountController implements AccountsApi
{
    @Autowired
    AccountDelegate accountDelegate;

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
}
