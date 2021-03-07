package org.pa.balance.user;

import org.keycloak.authorization.client.util.Http;
import org.pa.balance.account.repository.AccountDelegate;
import org.pa.balance.client.api.UsersApi;
import org.pa.balance.client.model.Account;
import org.pa.balance.client.model.AccountWrapper;
import org.pa.balance.client.model.User;
import org.pa.balance.client.model.UserAccountRights;
import org.pa.balance.error.UnauthorizedUserException;
import org.pa.balance.user.info.UserInfoProxy;
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

    @Autowired
    private UserInfoProxy userInfoProxy;

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

    @Override
    public ResponseEntity<List<User>> usersGet() {
        if (!userInfoProxy.isAuthenticatedUserGroupAdmin()) {
            throw new UnauthorizedUserException("Authenticated user does not have the \"group_admin\" role required to view all members of the group");
        }
        return new ResponseEntity<>(userDelegate.getAllUsersUnderGroupRestriction(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserAccountRights> usersUserIdAccountsAccountIdRightsGet(String userId, Long accountId) {
        if (!userId.equals(userInfoProxy.getAuthenticatedUser()) && !userInfoProxy.isAuthenticatedUserGroupAdmin()) {
            throw new UnauthorizedUserException(String.format("Authenticated user does not have the \"group_admin\" role required to view account rights for user %s", userId));
        }
        return new ResponseEntity<>(accountDelegate.fetchUserAccountRights(accountId, userId), HttpStatus.OK);
    }
}
