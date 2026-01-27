package org.qinyu.config.security.filter;

import org.qinyu.config.security.token.RegisterToken;
import org.qinyu.model.entry.input.RegisterEntry;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

public class RegisterFilter extends BaseAuthenticationFilter<RegisterEntry> {
    public RegisterFilter(AuthenticationManager manager, AuthenticationSuccessHandler successHandler,
                          AuthenticationFailureHandler failureHandler) {
        super(manager, successHandler, failureHandler, "/api/email/register");
    }

    @Override
    protected Authentication fromBody(RegisterEntry entry) {
        if (!StringUtils.hasText(entry.email())) {
            throw new AuthenticationCredentialsNotFoundException("请提供邮箱");
        }
        if (!StringUtils.hasText(entry.emailPassword())) {
            throw new AuthenticationCredentialsNotFoundException("请提供邮箱密码");
        }
        if (!StringUtils.hasText(entry.code())) {
            throw new AuthenticationCredentialsNotFoundException("请提供验证码");
        }
        return RegisterToken.unauthenticated(entry);
    }
}
