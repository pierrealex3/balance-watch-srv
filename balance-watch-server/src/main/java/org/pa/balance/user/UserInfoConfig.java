package org.pa.balance.user;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserInfoConfig
{
    @Bean
    public Keycloak kcClient(@Autowired KeycloakAdminClientProperties adminConfigProps, @Autowired KeycloakSpringBootProperties autoconfigProps) {
        return KeycloakBuilder.builder()
                .serverUrl(autoconfigProps.getAuthServerUrl())
                .realm(adminConfigProps.getRealm())
                .username(adminConfigProps.getUsername())
                .password(adminConfigProps.getPassword())
                .clientId(adminConfigProps.getClientId())
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();
    }

}
