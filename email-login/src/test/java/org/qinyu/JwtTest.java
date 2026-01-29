package org.qinyu;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.qinyu.model.UserModel;
import org.qinyu.util.TokenUtil;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@SpringBootTest
public class JwtTest {

    @Test
    public void testJwt() {
        UserModel userModel = new UserModel();
        userModel.setId(10);
        userModel.setUsername("admin");
        Map<String, Object> record = userModel.record();
        System.out.println("record：" + record);
        String token = TokenUtil.createToken(Map.of("typ", "JWT"), record);
        System.out.println("token：" + token);
        TokenUtil.parseToken(token);
        Jws<Claims> jws = TokenUtil.parseToken2(token);
        System.out.println("jws：" + jws);
    }

    @Test
    public void testSignature() {
        String secretKeyStr = "9Z8k7X6j5B4n3M2b1V0c9L8K7J6H5G4F3D2S1A0z9x8c7v6b5n4m3l2k1j0h9g8f7d6s5a4";
        String expectedIssuer = "qinyu";       // 配置中的iss
        String expectedSubject = "user-auth";  // 配置中的sub
        long expectedExpireSeconds = 7200;     // 配置中的exp=2h=7200秒
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpZCI6MTAsInVzZXJuYW1lIjoiYWRtaW4iLCJqdGkiOiJlYzgwZmQ1OGViNzk0YmJiYTU3ZTU2MDViZjNjMzc2MCIsImlzcyI6InFpbnl1Iiwic3ViIjoidXNlci1hdXRoIiwiaWF0IjoxNzY5NjcxMTc0LCJleHAiOjE3Njk2NzgzNzR9.IUmcNDV-wjj_AgWb5HCu65_ci44_UBzdUW3IjTOAIrMN-Qh0xO0ARv5LuIWjWJqFyHmev5BkscLWlkRs7T-J9A";
        try {
            // 3. 将字符串密钥转换为HS512所需的SecretKey（JJWT要求密钥长度至少512位）
            byte[] keyBytes = secretKeyStr.getBytes(StandardCharsets.UTF_8);
            SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

            // 4. 解析并验证JWT（自动验证签名、过期时间）
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(secretKey)          // 使用配置的key验证签名
                    .requireIssuer(expectedIssuer)     // 校验iss是否匹配
                    .requireSubject(expectedSubject)   // 校验sub是否匹配
                    .build()
                    .parseClaimsJws(token);

            // 5. 获取解析后的载荷信息
            Claims claims = jws.getBody();
            System.out.println("=== 签名验证成功，参数匹配 ===");
            System.out.println("Header中的算法: " + jws.getHeader().getAlgorithm()); // 输出HS512
            System.out.println("Payload中的id: " + claims.get("id"));             // 输出10
            System.out.println("Payload中的username: " + claims.get("username")); // 输出admin
            System.out.println("签发者(iss): " + claims.getIssuer());             // 输出qinyu
            System.out.println("主题(sub): " + claims.getSubject());             // 输出user-auth
            System.out.println("签发时间(iat): " + new Date(claims.getIssuedAt().getTime()));
            System.out.println("过期时间(exp): " + new Date(claims.getExpiration().getTime()));

            // 验证过期时长是否为2小时
            long expireDuration = claims.getExpiration().getTime() - claims.getIssuedAt().getTime();
            System.out.println("过期时长: " + (expireDuration / 1000) + "秒（预期7200秒）");
            System.out.println("过期时长是否匹配: " + (expireDuration / 1000 == expectedExpireSeconds));

        } catch (Exception e) {
            System.err.println("=== 验证失败 ===");
            System.err.println("失败原因: " + e.getMessage());
        }
    }
}
