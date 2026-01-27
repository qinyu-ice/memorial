package org.qinyu.config.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.qinyu.config.security.token.JwtAuthenticationToken;
import org.qinyu.service.RedisService;
import org.qinyu.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final RedisService service;
    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> source =
            new WebAuthenticationDetailsSource();

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(header)) {
            filterChain.doFilter(request, response);
            return;
        }
        setSecurityContext(header(header), request);
        filterChain.doFilter(request, response);
    }

    private String header(String header) {
        List<String> authorization = Arrays.asList(header.split(" "));
        if (authorization.size() < 2) {
            throw new BadCredentialsException("除访问密钥外, 还请提供对应的认证方式");
        }
        if (!"Bearer".equals(authorization.getFirst())) {
            throw new BadCredentialsException("错误的认证方式, 请确保使用 Bearer 认证");
        }
        return authorization.getLast();
    }

    private void setSecurityContext(String jwt, HttpServletRequest request) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(generate(jwt, request));
        SecurityContextHolder.setContext(context);
    }

    @SuppressWarnings(value = "unchecked")
    private Authentication generate(String jwt, HttpServletRequest request) {
        try {
            Jws<Claims> jws = JwtUtil.parse(jwt);
            String keyId = jws.getHeader().getKeyId();
            if (service.isBlack(keyId)) {
                throw new JwtException(null);
            }
            Claims payload = jws.getPayload();
            return JwtAuthenticationToken.authenticated(keyId, payload.get("email", String.class),
                    source.buildDetails(request), payload.getExpiration().getTime() - System.currentTimeMillis(),
                    payload.get("authorities", List.class).stream()
                            .map(authority -> (GrantedAuthority) (authority::toString))
                            .toList());
        } catch (JwtException exception) {
            throw new BadCredentialsException("该 jwt 已失效, 请重新登录以获取新的 jwt");
        }
    }
}
