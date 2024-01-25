package com.sojoo.StoreSpotter.jwt.config;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String issuer;
    private String secretKey;
}
