package org.pa.balance.user.info;

import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.GroupRepresentation;
import org.pa.balance.user.KeycloakAdminClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserInfoDelegate
{
    @Autowired
    Keycloak keycloakProxy;

    @Autowired
    KeycloakSpringBootProperties autoconfigProps;

    public static final String KC_TGT_GRP_PREFIX = "MDS_";

    /**
     * Returns the target group associated to the user, of null if the user is not part of any group.
     * Note: the target group starts with "MDS_"
     * Note: when using that method, the user must exist in KeyCloak, otherwise a RuntimeException will be thrown legit.
     * @param userInternalId the user id INTERNAL to KeyCloak
     * @return the group INTERNAL ID or an empty Optional if user is not assigned to any group (legit config).
     */
    Optional<String> getTargetGroup(String userInternalId) {
        return keycloakProxy.realm(autoconfigProps.getRealm()).users().get(userInternalId).groups().stream()
                .filter(g -> g.getName().startsWith(KC_TGT_GRP_PREFIX)).map(GroupRepresentation::getId).findFirst();
    }

    /**
     * Returns the user names i.e. "KeyCloak logins" associated to the group provided.
     * This method is essential for group->users knowledge from this app, which does not contain any user-related info.
     * @param groupId
     * @return
     */
    List<String> getUsersForGroup(String groupId) {
        return keycloakProxy.realm(autoconfigProps.getRealm()).groups().group(groupId).members().stream().map( userRepresentation -> userRepresentation.getUsername() ).collect(
                Collectors.toList());
    }
}
