package org.qinyu.config.security.filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.qinyu.util.ClassUtil;
import org.qinyu.util.JsonUtil;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseAuthenticationFilter<E> extends AbstractAuthenticationProcessingFilter {
    protected BaseAuthenticationFilter(AuthenticationManager manager, AuthenticationSuccessHandler successHandler,
                                       AuthenticationFailureHandler failureHandler, String processing) {
        super(processing, manager);
        setAuthenticationSuccessHandler(successHandler);
        setAuthenticationFailureHandler(failureHandler);
    }

    protected abstract Authentication fromBody(E entry);

    protected Authentication fromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            throw new AuthenticationCredentialsNotFoundException("未携带 jwt 密钥");
        }
        return authentication;
    }

    protected HttpMethod method() {
        return HttpMethod.POST;
    }

    protected MediaType contentType() {
        return MediaType.APPLICATION_JSON;
    }

    @Override
    public @NonNull Authentication attemptAuthentication(@NonNull HttpServletRequest request,
                                                         @NonNull HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        checkMethodAndContentType(request);
        Class<E> clazz = ClassUtil.getGenericClass(getClass());
        Authentication authentication = clazz
                .isAssignableFrom(Object.class) ? fromContext() : fromBody(request, clazz);
        AbstractAuthenticationToken token = (AbstractAuthenticationToken) authentication;
        token.setDetails(authenticationDetailsSource.buildDetails(request));
        return getAuthenticationManager().authenticate(token);
    }

    private void checkMethodAndContentType(HttpServletRequest request) {
        String method = request.getMethod();
        if (!method().matches(method)) {
            throw new AuthenticationServiceException("请选择正确的请求方式, 而非 " + method + " 请求");
        }
        String contentType = request.getContentType();
        if (!contentType().includes(MediaType.valueOf(contentType))) {
            throw new AuthenticationServiceException("请选择正确的内容类型, 而非 " + contentType);
        }
    }

    private Authentication fromBody(HttpServletRequest request, Class<E> clazz) {
        try (BufferedReader reader = request.getReader()) {
            String body = reader.lines().collect(Collectors.joining());
            return fromBody(JsonUtil.parse(body, clazz));
        } catch (IOException exception) {
            log.error(exception.getLocalizedMessage(), exception);
            throw new InternalAuthenticationServiceException(request.getRequestURI() + " 处理繁忙, 请稍后再试");
        }
    }
}
