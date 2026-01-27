package org.qinyu.config.security.filter;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class LogoutFilter extends BaseAuthenticationFilter<Object> {
    public LogoutFilter(AuthenticationManager manager, AuthenticationSuccessHandler successHandler,
                        AuthenticationFailureHandler failureHandler) {
        super(manager, successHandler, failureHandler, "/api/email/logout");
    }

    @Override
    protected HttpMethod method() {
        return HttpMethod.PUT;
    }

    @Override
    protected MediaType contentType() {
        return MediaType.TEXT_PLAIN;
    }

    @Override
    protected Authentication fromBody(Object entry) {
        return null;
    }
}
