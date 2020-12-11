package org.pa.balance.user.info;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserInfoProxy
{
    @Autowired
    private UserInfoDelegate userInfoDelegate;

    public Optional<String> getAuthenticatedUserTargetGroup() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        String userInternalId = ((KeycloakPrincipal)((KeycloakAuthenticationToken)a).getPrincipal()).getKeycloakSecurityContext().getToken().getSubject();
        return userInfoDelegate.getTargetGroup(userInternalId);
    }

    public List<String> getUsersForGroup(String internalGroupId) {
        return userInfoDelegate.getUsersForGroup(internalGroupId);
    }
}
