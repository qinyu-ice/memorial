package org.qinyu.config.security.token;

import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class JwtAuthenticationToken extends BaseAuthenticationToken {
    private final Long timeout;

    protected JwtAuthenticationToken(String principal, String credentials, Object details, Long timeout,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.timeout = timeout;
        setDetails(details);
    }

    @Override
    public @NonNull String getPrincipal() {
        return (String) super.getPrincipal();
    }

    public static JwtAuthenticationToken authenticated(String keyId, String email, Object details, Long timeout,
                                                       Collection<? extends GrantedAuthority> authorities) {
        return new JwtAuthenticationToken(keyId, email, details, timeout, authorities);
    }

    public static JwtAuthenticationToken logout(JwtAuthenticationToken token) {
        return new JwtAuthenticationToken((String) token.getCredentials(), token.getPrincipal(),
                token.getDetails(), token.getTimeout(), token.getAuthorities());
    }
}
