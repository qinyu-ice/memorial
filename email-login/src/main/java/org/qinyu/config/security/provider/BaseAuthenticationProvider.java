package org.qinyu.config.security.provider;

import org.jspecify.annotations.NonNull;
import org.qinyu.util.ClassUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public abstract class BaseAuthenticationProvider<T> implements AuthenticationProvider {

    @Override
    @SuppressWarnings(value = "unchecked")
    public @NonNull Authentication authenticate(@NonNull Authentication authentication)
            throws AuthenticationException {
        return authentication((T) authentication);
    }

    protected abstract @NonNull Authentication authentication(T token);

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return ClassUtil.getGenericClass(getClass()).isAssignableFrom(authentication);
    }
}
