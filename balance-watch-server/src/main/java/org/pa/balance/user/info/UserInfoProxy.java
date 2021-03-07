package org.pa.balance.user.info;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
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

    public String getAuthenticatedUser() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        return ((KeycloakPrincipal)((KeycloakAuthenticationToken)a).getPrincipal()).getName();
    }

    public boolean isAuthenticatedUserGroupAdmin() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        return ((KeycloakAuthenticationToken)a).getAuthorities().stream().filter(Objects::nonNull).anyMatch( ga -> "ROLE_group_admin".equals(ga.getAuthority()) );
    }
}
