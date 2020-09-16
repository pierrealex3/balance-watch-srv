package org.pa.balance.user;

import org.pa.balance.account.repository.AccountDelegate;
import org.pa.balance.client.api.UsersApi;
import org.pa.balance.client.model.Account;
import org.pa.balance.client.model.AccountWrapper;
import org.pa.balance.client.model.User;
import org.pa.balance.client.model.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController implements UsersApi
{
    @Autowired
    private UserDelegate userDelegate;

    @Autowired
    private AccountDelegate accountDelegate;

    @Override
    public ResponseEntity<Void> usersPost(@Valid User body)
    {
        Long id = userDelegate.addUser(body);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("X-Internal-Id", String.valueOf(id));

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserWrapper> usersUserIdGet(Long userId)
    {
        UserWrapper u = userDelegate.getUser(userId);
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> usersUserIdPut(Long userId, @Valid User body)
    {
        userDelegate.updateUser(userId, body);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> usersUserIdAccountsPost(Long userId, @Valid Account body)
    {
        Long id = accountDelegate.addAccount(userId, body);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("X-Internal-Id", String.valueOf(id));

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * this method does not include the account rights
     * @param userId  (required)
     * @return
     */
    @Override
    public ResponseEntity<List<AccountWrapper>> usersUserIdAccountsGet(Long userId)
    {
        List<AccountWrapper> accountWrapperList = accountDelegate.getAccounts(userId);
        return new ResponseEntity<>(accountWrapperList, HttpStatus.OK);
    }
}
