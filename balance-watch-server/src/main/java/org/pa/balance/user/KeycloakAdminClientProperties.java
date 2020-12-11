package org.pa.balance.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class ain't used by the SpringBoot-Keycloak autoconfig.
 * @see org.keycloak.adapters.springboot.KeycloakSpringBootProperties
 */
@Configuration
@ConfigurationProperties(prefix = "keycloakx")
@Getter
@Setter
public class KeycloakAdminClientProperties
{
    private String realm;

    private String authServerUrl;

    private String username;

    private String password;

    private String clientId;
}