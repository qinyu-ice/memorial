package org.qinyu.config;

import org.qinyu.handler.exception.BaseException;
import org.qinyu.handler.UserHandler;
import org.qinyu.model.api.Result;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(UserHandler userHandler) {
        return RouterFunctions.route()
                .path("/email", builder -> builder
                        .GET("/{id}", userHandler::findById))
                .filter((request, next) -> {
                    try {
                        return next.handle(request);
                    } catch (BaseException exception) {
                        return build(HttpStatus.BAD_REQUEST, exception);
                    } catch (Exception exception) {
                        return build(HttpStatus.INTERNAL_SERVER_ERROR, exception);
                    }
                })
                .build();
    }

    private ServerResponse build(HttpStatus status, Exception exception) {
        Result<Object> result = Result.build(status, exception.getLocalizedMessage(), null);
        return ServerResponse.status(status).body(result);
    }
}
