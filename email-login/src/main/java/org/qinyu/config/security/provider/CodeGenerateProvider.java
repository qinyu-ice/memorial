package org.qinyu.config.security.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.qinyu.config.security.token.CodeGenerateToken;
import org.qinyu.service.MailService;
import org.qinyu.service.RedisService;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import java.security.SecureRandom;

@Slf4j
@RequiredArgsConstructor
public class CodeGenerateProvider extends BaseAuthenticationProvider<CodeGenerateToken> {
    private final RedisService redisService;
    private final MailService mailService;
    private final SecureRandom random;

    @Override
    protected @NonNull Authentication authentication(CodeGenerateToken token) {
        send(token.getPrincipal(), random.nextInt(100000, 1000000));
        return CodeGenerateToken.authenticated(token);
    }

    private void send(String email, Integer code) {
        try {
            String value = String.valueOf(code);
            redisService.saveAuthCode(email, value);
            mailService.sendAuthCode(email, value);
        } catch (MailSendException exception) {
            log.warn(exception.getLocalizedMessage());
            throw new BadCredentialsException("无法发送验证码至 " + email + ", 请检查邮箱拼写是否正确");
        }
    }
}
