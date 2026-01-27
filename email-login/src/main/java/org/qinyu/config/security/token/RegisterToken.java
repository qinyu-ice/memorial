package org.qinyu.config.security.token;

import org.qinyu.model.entry.input.RegisterEntry;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class RegisterToken extends BaseAuthenticationToken {
    private static final List<GrantedAuthority> AUTHORITIES = List.of(() -> "REGISTER");

    private RegisterToken(Object principal) {
        super(principal, BaseAuthenticationToken.CREDENTIALS, AUTHORITIES);
    }

    public static RegisterToken unauthenticated(RegisterEntry entry) {
        return new RegisterToken(entry);
    }

    public static RegisterToken authenticated(RegisterToken token) {
        token.setAuthenticated(true);
        return token;
    }
}
