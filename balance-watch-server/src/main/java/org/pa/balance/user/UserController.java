package org.pa.balance.user;

import org.pa.balance.account.repository.AccountDelegate;
import org.pa.balance.client.api.UsersApi;
import org.pa.balance.client.model.Account;
import org.pa.balance.client.model.AccountWrapper;
import org.pa.balance.client.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
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
        String id = userDelegate.addUser(body);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("X-Internal-Id", id);

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<User> usersUserIdGet(String userId)
    {
        User u = userDelegate.getUser(userId);
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> usersUserIdAccountsPost(String userId, @Valid Account body)
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
    public ResponseEntity<List<AccountWrapper>> usersUserIdAccountsGet(String userId)
    {
        List<AccountWrapper> accountWrapperList = accountDelegate.getAccounts(userId);
        return new ResponseEntity<>(accountWrapperList, HttpStatus.OK);
    }


    @RequestMapping(value = "/users/{userId}/relatedAccounts",
            produces = { "application/json" },
            method = RequestMethod.GET)
    public ResponseEntity<List<AccountWrapper>> usersUserIdAccountsGetRelated(@PathVariable("userId") String userId)
    {
        List<AccountWrapper> accountWrapperList = accountDelegate.getRelatedAccounts(userId);
        return new ResponseEntity<>(accountWrapperList, HttpStatus.OK);
    }
}
