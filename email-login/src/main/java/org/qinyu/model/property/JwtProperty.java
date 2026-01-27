package org.qinyu.model.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "spring.security.jjwt")
public record JwtProperty(String secret, String expiration) {
}
