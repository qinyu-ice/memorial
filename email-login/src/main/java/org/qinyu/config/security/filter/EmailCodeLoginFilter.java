package org.qinyu.config.security.filter;

import org.qinyu.config.security.token.EmailCodeLoginToken;
import org.qinyu.model.entry.input.EmailCodeLoginEntry;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

public class EmailCodeLoginFilter extends BaseAuthenticationFilter<EmailCodeLoginEntry> {
    public EmailCodeLoginFilter(AuthenticationManager manager, AuthenticationSuccessHandler successHandler,
                                AuthenticationFailureHandler failureHandler) {
        super(manager, successHandler, failureHandler, "/api/email/code-login");
    }

    @Override
    protected Authentication fromBody(EmailCodeLoginEntry entry) {
        if (!StringUtils.hasText(entry.email())) {
            throw new AuthenticationCredentialsNotFoundException("请提供邮箱");
        }
        if (!StringUtils.hasText(entry.code())) {
            throw new AuthenticationCredentialsNotFoundException("请提供验证码");
        }
        return EmailCodeLoginToken.unauthenticated(entry);
    }
}
