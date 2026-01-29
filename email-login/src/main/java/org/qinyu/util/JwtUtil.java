package org.qinyu.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.qinyu.model.property.JwtProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class JwtUtil {
    private static final Header HEADER = Jwts.header().type("JWT").build();
    private static final Claims CLAIMS = Jwts.claims().issuer("qinyu").build();
    private static final Pattern PATTERN = Pattern.compile("(\\d+)[mhd]?");

    private static JwtProperty property;
    private static SecretKey key;

    @Autowired
    public void setProperty(JwtProperty property) {
        JwtUtil.property = property;
        initKey(property.secret());
    }

    private void initKey(String secret) {
        byte[] bytes = Base64.getDecoder().decode(secret);
        JwtUtil.key = Keys.hmacShaKeyFor(bytes);
    }

    public static String generate(Map<String, Object> payload) {
        ZonedDateTime now = ZonedDateTime.now();
        String keyId = UUID.randomUUID().toString();
        return Jwts.builder()
                .header()
                .add(HEADER)
                .keyId(keyId.replaceAll("-", ""))
                .and()
                .claims()
                .add(CLAIMS)
                .add(payload)
                .issuedAt(Date.from(now.toInstant()))
                .expiration(toDate(now, property.expiration()))
                .and()
                .signWith(key)
                .compact();
    }

    private static Date toDate(ZonedDateTime zonedDateTime, String expiration) {
        Matcher matcher = PATTERN.matcher(expiration);
        if (!matcher.matches()) {
            log.warn("配置项 spring.security.jjwt.expiration 不符合规范, 已使用默认值 1h");
            Instant instant = zonedDateTime.plusHours(1).toInstant();
            return Date.from(instant);
        }
        String amount = matcher.group(1);
        Instant instant = (switch (expiration.substring(amount.length())) {
            case "m" -> zonedDateTime.plusMinutes(Long.parseLong(amount));
            case "h" -> zonedDateTime.plusHours(Long.parseLong(amount));
            case "d" -> zonedDateTime.plusDays(Long.parseLong(amount));
            default -> zonedDateTime.plusSeconds(Long.parseLong(amount));
        }).toInstant();
        return Date.from(instant);
    }

    public static Jws<Claims> parse(String jwt) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt);
    }
}
