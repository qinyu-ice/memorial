package org.qinyu.model.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "spring.data.redis.properties")
public record RedisProperty(Prefix prefix, Integer timeout) {
    public record Prefix(String authCode, String blacklistJwt) {
    }
}
