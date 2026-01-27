package org.qinyu.config.security.provider;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.qinyu.config.security.token.JwtAuthenticationToken;
import org.qinyu.service.RedisService;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
public class LogoutProvider extends BaseAuthenticationProvider<JwtAuthenticationToken> {
    private final RedisService service;

    @Override
    protected @NonNull Authentication authentication(JwtAuthenticationToken token) {
        service.addToBlacklist(token.getPrincipal(), (String) token.getCredentials(), token.getTimeout());
        return JwtAuthenticationToken.logout(token);
    }
}
