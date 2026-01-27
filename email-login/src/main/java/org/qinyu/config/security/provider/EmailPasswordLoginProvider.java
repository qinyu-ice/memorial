package org.qinyu.config.security.provider;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.qinyu.config.security.token.EmailPasswordLoginToken;
import org.qinyu.model.UserModel;
import org.qinyu.model.entry.input.EmailPasswordLoginEntry;
import org.qinyu.service.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class EmailPasswordLoginProvider extends BaseAuthenticationProvider<EmailPasswordLoginToken> {
    private final UserService service;
    private final PasswordEncoder encoder;

    @Override
    protected @NonNull Authentication authentication(EmailPasswordLoginToken token) {
        EmailPasswordLoginEntry entry = (EmailPasswordLoginEntry) token.getPrincipal();
        UserModel model = (UserModel) service.loadUserByUsername(entry.email());
        if (!encoder.matches(entry.emailPassword(), model.getPassword())) {
            throw new BadCredentialsException("邮箱或密码有误");
        }
        return EmailPasswordLoginToken.authenticated(model);
    }
}
