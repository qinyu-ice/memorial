package org.qinyu.config.security.token;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class CodeGenerateToken extends BaseAuthenticationToken {
    private static final List<GrantedAuthority> AUTHORITIES = List.of(() -> "CODE_GENERATE");

    private CodeGenerateToken(String principal) {
        super(principal, BaseAuthenticationToken.CREDENTIALS, AUTHORITIES);
    }

    @Override
    public @NonNull String getPrincipal() {
        return (String) super.getPrincipal();
    }

    public static CodeGenerateToken unauthenticated(String email) {
        return new CodeGenerateToken(email);
    }

    public static CodeGenerateToken authenticated(CodeGenerateToken token) {
        token.setAuthenticated(true);
        return token;
    }
}
