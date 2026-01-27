package org.qinyu.config.security.token;

import org.qinyu.model.UserModel;
import org.qinyu.model.entry.input.EmailCodeLoginEntry;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class EmailCodeLoginToken extends BaseAuthenticationToken {
    private EmailCodeLoginToken(Object principal, Object credentials,
                                Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static EmailCodeLoginToken unauthenticated(EmailCodeLoginEntry entry) {
        return new EmailCodeLoginToken(entry, entry.code(), AuthorityUtils.NO_AUTHORITIES);
    }

    public static EmailCodeLoginToken authenticated(UserModel model) {
        EmailCodeLoginToken token = new EmailCodeLoginToken(model, model.getPassword(), model.getAuthorities());
        token.setAuthenticated(true);
        return token;
    }
}
