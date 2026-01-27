package org.qinyu.config.security.provider;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.qinyu.config.security.token.EmailCodeLoginToken;
import org.qinyu.model.UserModel;
import org.qinyu.model.entry.input.EmailCodeLoginEntry;
import org.qinyu.service.RedisService;
import org.qinyu.service.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
public class EmailCodeLoginProvider extends BaseAuthenticationProvider<EmailCodeLoginToken> {
    private final RedisService redisService;
    private final UserService userService;

    @Override
    protected @NonNull Authentication authentication(EmailCodeLoginToken token) {
        EmailCodeLoginEntry entry = (EmailCodeLoginEntry) token.getPrincipal();
        if (!redisService.verifyAuthCode(entry.email(), entry.code())) {
            throw new BadCredentialsException("邮箱或验证码有误");
        }
        UserModel model = (UserModel) userService.loadUserByUsername(entry.email());
        return EmailCodeLoginToken.authenticated(model);
    }
}
