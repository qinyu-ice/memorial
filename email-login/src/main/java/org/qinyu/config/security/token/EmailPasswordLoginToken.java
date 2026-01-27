package org.qinyu.config.security.token;

import org.qinyu.model.UserModel;
import org.qinyu.model.entry.input.EmailPasswordLoginEntry;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class EmailPasswordLoginToken extends BaseAuthenticationToken {
    private EmailPasswordLoginToken(Object principal, Object credentials,
                                    Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static EmailPasswordLoginToken unauthenticated(EmailPasswordLoginEntry entry) {
        return new EmailPasswordLoginToken(entry, entry.emailPassword(), AuthorityUtils.NO_AUTHORITIES);
    }

    public static EmailPasswordLoginToken authenticated(UserModel model) {
        EmailPasswordLoginToken token = new EmailPasswordLoginToken(model, model.getPassword(), model.getAuthorities());
        token.setAuthenticated(true);
        return token;
    }
}
