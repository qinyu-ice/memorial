package org.qinyu.config.security.provider;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.qinyu.config.security.token.RegisterToken;
import org.qinyu.model.UserModel;
import org.qinyu.model.entry.input.RegisterEntry;
import org.qinyu.service.RedisService;
import org.qinyu.service.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.UUID;

@RequiredArgsConstructor
public class RegisterProvider extends BaseAuthenticationProvider<RegisterToken> {
    private final RedisService redisService;
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final SecureRandom random;

    @Override
    protected @NonNull Authentication authentication(RegisterToken token) {
        RegisterEntry entry = (RegisterEntry) token.getPrincipal();
        if (!redisService.verifyAuthCode(entry.email(), entry.code())) {
            throw new BadCredentialsException("邮箱或验证码有误");
        }
        userService.createUser(generate(entry));
        return RegisterToken.authenticated(token);
    }

    private UserModel generate(RegisterEntry entry) {
        UserModel model = UserModel.withEmail(entry.email());
        if (StringUtils.hasText(entry.username())) {
            model.setUsername(entry.username());
        } else {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String nick = uuid.substring(random.nextInt(0, uuid.length() - 10));
            model.setUsername(nick);
        }
        model.setEmailPassword(encoder.encode(entry.emailPassword()));
        return model;
    }
}
