package org.qinyu.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.qinyu.entity.User;
import org.qinyu.expcetion.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TokenUtil {

    private TokenUtil() {
    }

    // 加密/解密 密钥
    private static String key;

    @Value(value = "${token.key}")
    private void setKey(String key) {
        TokenUtil.key = key;
    }

    // token 签发者
    private static String iss;

    @Value(value = "${token.iss}")
    private void setIss(String iss) {
        TokenUtil.iss = iss;
    }

    // token 主题
    private static String sub;

    @Value(value = "${token.sub}")
    private void setSub(String sub) {
        TokenUtil.sub = sub;
    }

    // token 过期时间
    private static String exp;

    @Value(value = "${token.exp}")
    private void setExp(String exp) {
        TokenUtil.exp = exp;
    }

    // token 过期时间的正则表达式
    private static final Pattern EXP_PATTERN = Pattern.compile("(\\d+)([mshd]{1,2})?");

    /**
     * 使用用户数据创建 token
     *
     * @param user 用户数据
     * @return token
     */
    public static Map<String, Object> createToken(User user) {
        return Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "token", createToken(Map.of("typ", "JWT"), Map.of(
                        "id", user.getId(),
                        "username", user.getUsername()
                ))
        );
    }

    /**
     * 使用自定义头部和自定义负载创建 token
     *
     * @param header  自定义头部
     * @param payload 自定义负载
     * @return token
     */
    public static String createToken(Map<String, Object> header, Map<String, Object> payload) {
        return Jwts.builder()
                .header()
                .add(header)
                .and()
                .claims(payload)
                .id(UUID.randomUUID().toString().replace("-", ""))
                .issuer(iss)
                .subject(sub)
                .issuedAt(convert2Date("0"))
                .expiration(convert2Date(exp))
                .signWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    /**
     * 解析字符串格式的过期时间，并以 Date 对象返回
     *
     * @param exp 字符串格式的过期时间
     * @return Date 对象格式的过期时间
     */
    private static Date convert2Date(String exp) {
        Matcher matcher = EXP_PATTERN.matcher(exp);
        if (!matcher.matches()) throw new CustomException("无效的 token 过期时间: " + exp);
        Calendar calendar = Calendar.getInstance();
        calendar.add(switch (matcher.group(2)) {
            case "s" -> Calendar.SECOND;
            case "m" -> Calendar.MINUTE;
            case "h" -> Calendar.HOUR;
            case "d" -> Calendar.DAY_OF_YEAR;
            case null, default -> Calendar.MILLISECOND;
        }, Integer.parseInt(matcher.group(1)));
        return calendar.getTime();
    }

    /**
     * 解析 token 并返回获得的数据
     *
     * @param token token
     */
    public static void parseToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            throw new CustomException("token 已过期...");
        } catch (SignatureException | MalformedJwtException e) {
            throw new CustomException("token 被篡改...");
        }
    }

    /**
     * 解析 token 并返回 Jws<Claims> 对象（关键修复：添加返回值）
     *
     * @param token token字符串
     * @return 解析后的Jws<Claims>对象
     */
    public static Jws<Claims> parseToken2(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            throw new CustomException("token 已过期...");
        } catch (SignatureException | MalformedJwtException e) {
            throw new CustomException("token 被篡改...");
        } catch (JwtException e) { // 补充捕获其他JWT异常
            throw new CustomException("token 解析失败: " + e.getMessage());
        }
    }
}
