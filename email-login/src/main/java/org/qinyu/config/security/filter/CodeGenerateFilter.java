package org.qinyu.config.security.filter;

import org.qinyu.config.security.token.CodeGenerateToken;
import org.qinyu.model.entry.input.CodeGenerateEntry;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

public class CodeGenerateFilter extends BaseAuthenticationFilter<CodeGenerateEntry> {
    public CodeGenerateFilter(AuthenticationManager manager, AuthenticationSuccessHandler successHandler,
                              AuthenticationFailureHandler failureHandler) {
        super(manager, successHandler, failureHandler, "/api/email/code");
    }

    @Override
    protected Authentication fromBody(CodeGenerateEntry entry) {
        String email = entry.email();
        if (!StringUtils.hasText(email)) {
            throw new AuthenticationCredentialsNotFoundException("请提供邮箱");
        }
        return CodeGenerateToken.unauthenticated(email);
    }
}
