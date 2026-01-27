package org.qinyu.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.qinyu.config.security.filter.*;
import org.qinyu.config.security.provider.*;
import org.qinyu.config.security.token.*;
import org.qinyu.model.UserModel;
import org.qinyu.model.api.Result;
import org.qinyu.model.entry.input.RegisterEntry;
import org.qinyu.service.MailService;
import org.qinyu.service.RedisService;
import org.qinyu.service.UserService;
import org.qinyu.util.JsonUtil;
import org.qinyu.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2B);
    }

    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }

    @Bean
    public AuthenticationManager authenticationManager(RedisService redisService, UserService userService,
                                                       MailService mailService, PasswordEncoder passwordEncoder,
                                                       SecureRandom secureRandom) {
        return new ProviderManager(List.of(
                new CodeGenerateProvider(redisService, mailService, secureRandom),
                new EmailCodeLoginProvider(redisService, userService),
                new EmailPasswordLoginProvider(userService, passwordEncoder),
                new LogoutProvider(redisService),
                new RegisterProvider(redisService, userService, passwordEncoder, secureRandom)
        ));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager manager,
                                                   RedisService redisService) {
        return httpSecurity

                // 定义授权规则
                .authorizeHttpRequests(registry -> registry
                        .anyRequest().authenticated())

                // 关闭 csrf 防护, 纯 api 场景不需要
                .csrf(AbstractHttpConfigurer::disable)

                // 关闭请求重定向缓存
                .requestCache(AbstractHttpConfigurer::disable)

                // 关闭 httpBasic 认证
                .httpBasic(AbstractHttpConfigurer::disable)

                // 关闭表单登录登出
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)

                // 关闭记住我
                .rememberMe(AbstractHttpConfigurer::disable)

                // 无状态 session, 但保留会话安全策略
                .sessionManagement(config -> config
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 关闭用户匿名身份发放
                .anonymous(AbstractHttpConfigurer::disable)

                // 定义接口异常访问应对方案
                .exceptionHandling(config -> config
                        .authenticationEntryPoint(this::authenticationHandler)
                        .accessDeniedHandler(this::authenticationHandler))

                // 添加自定义 JwtAuthenticationFilter
                .addFilterAfter(new JwtAuthenticationFilter(redisService), ExceptionTranslationFilter.class)

                // 添加自定义 CodeGenerateFilter
                .addFilterAfter(new CodeGenerateFilter(manager, this::authenticationHandler,
                        this::authenticationHandler), ExceptionTranslationFilter.class)

                // 添加自定义 EmailCodeAuthenticationFilter
                .addFilterAfter(new EmailCodeLoginFilter(manager, this::authenticationHandler,
                        this::authenticationHandler), ExceptionTranslationFilter.class)

                // 添加自定义 EmailPasswordAuthenticationFilter
                .addFilterAfter(new EmailPasswordLoginFilter(manager, this::authenticationHandler,
                        this::authenticationHandler), ExceptionTranslationFilter.class)

                // 添加自定义 LogoutFilter
                .addFilterAfter(new LogoutFilter(manager, this::authenticationHandler,
                        this::authenticationHandler), ExceptionTranslationFilter.class)

                // 添加自定义 RegisterFilter
                .addFilterAfter(new RegisterFilter(manager, this::authenticationHandler,
                        this::authenticationHandler), ExceptionTranslationFilter.class)

                // 构建过滤器链
                .build();
    }

    private <T> void authenticationHandler(HttpServletRequest request, HttpServletResponse response, T object) {
        response.setCharacterEncoding(StandardCharsets.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JsonUtil.generate(switch (object) {
                case Authentication authentication -> switch (authentication) {
                    case CodeGenerateToken token -> {
                        String message = "验证码已成功发送至 " + token.getPrincipal();
                        yield messageResponse(message, response);
                    }
                    case EmailCodeLoginToken emailCodeLoginToken ->
                            jwtResponse(authentication.getPrincipal(), response);
                    case EmailPasswordLoginToken emailPasswordLoginToken ->
                            jwtResponse(authentication.getPrincipal(), response);
                    case JwtAuthenticationToken token -> {
                        String message = "用户 " + token.getPrincipal() + " 已成功登出";
                        yield messageResponse(message, response);
                    }
                    case RegisterToken token -> {
                        String message = "用户 " + ((RegisterEntry) token.getPrincipal()).email() + " 注册成功";
                        yield messageResponse(message, response);
                    }
                    default -> defaultResponse(response);
                };
                case AuthenticationException exception ->
                        exceptionResponse(HttpStatus.UNAUTHORIZED, exception, response);
                case AccessDeniedException exception -> exceptionResponse(HttpStatus.FORBIDDEN, exception, response);
                default -> defaultResponse(response);
            }));
        } catch (IOException exception) {
            log.error(exception.getLocalizedMessage(), exception);
        }
    }

    private Result<Object> defaultResponse(HttpServletResponse response) {
        return messageResponse(null, response);
    }

    private Result<Object> jwtResponse(Object principal, HttpServletResponse response) {
        UserModel userModel = (UserModel) principal;
        Map<String, Object> payload = userModel.payload();
        userModel.setJwt(JwtUtil.generate(payload));
        return defaultResponse(HttpStatus.OK, null, userModel, response);
    }

    private Result<Object> messageResponse(String message, HttpServletResponse response) {
        return defaultResponse(HttpStatus.OK, message, null, response);
    }

    private Result<Object> exceptionResponse(HttpStatus status, Exception exception, HttpServletResponse response) {
        return defaultResponse(status, exception.getLocalizedMessage(), null, response);
    }

    private Result<Object> defaultResponse(HttpStatus status, String message, Object data, HttpServletResponse response) {
        response.setStatus(status.value());
        return Result.build(status, message, data);
    }
}
