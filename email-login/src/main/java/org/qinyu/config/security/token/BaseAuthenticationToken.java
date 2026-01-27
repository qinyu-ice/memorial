package org.qinyu.config.security.token;

import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public abstract class BaseAuthenticationToken extends AbstractAuthenticationToken {
    protected static final Object CREDENTIALS = new Object();
    protected final @NonNull Object principal;
    protected @NonNull Object credentials;

    protected BaseAuthenticationToken(@NonNull Object principal, @NonNull Object credentials,
                                      @NonNull Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(!authorities.isEmpty());
    }

    @Override
    public @NonNull Object getPrincipal() {
        return principal;
    }

    @Override
    public @NonNull Object getCredentials() {
        return credentials;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = CREDENTIALS;
    }
}
