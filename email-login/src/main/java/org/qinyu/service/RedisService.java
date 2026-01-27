package org.qinyu.service;

import lombok.RequiredArgsConstructor;
import org.qinyu.model.property.RedisProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisProperty property;
    private final StringRedisTemplate redisTemplate;

    public void saveAuthCode(String email, String code) {
        String key = property.prefix().authCode().concat(email);
        redisTemplate.opsForValue().set(key, code, Duration.ofMinutes(property.timeout()));
    }

    public boolean verifyAuthCode(String email, String code) {
        String key = property.prefix().authCode().concat(email);
        if (!redisTemplate.hasKey(key)) {
            return false;
        }
        String savedCode = redisTemplate.opsForValue().get(key);
        return code.equals(savedCode) && redisTemplate.delete(key);
    }

    public void addToBlacklist(String keyId, String jwt, Long timeout) {
        String key = property.prefix().blacklistJwt().concat(keyId);
        redisTemplate.opsForValue().set(key, jwt, Duration.ofMillis(timeout));
    }

    public boolean isBlack(String keyId) {
        return redisTemplate.hasKey(property.prefix().blacklistJwt().concat(keyId));
    }
}
