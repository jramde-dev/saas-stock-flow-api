package dev.jramde.saas.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JrJwtProperties {
    private String privateKeyPath;
    private String publicKeyPath;
    private long accessTokenExpiration;
}
