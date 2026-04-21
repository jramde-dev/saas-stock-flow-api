package dev.jramde.saas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
public class JrBeanConfig {

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfig) {
        return authenticationConfig.getAuthenticationManager();
    }
}
