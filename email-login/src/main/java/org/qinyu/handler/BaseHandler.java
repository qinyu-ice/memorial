package org.qinyu.handler;

import org.qinyu.model.api.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Objects;

public abstract class BaseHandler {
    private static final ServerResponse.BodyBuilder BUILDER = ServerResponse.ok();

    protected <T> ServerResponse ok(T data) {
        HttpStatus status = Objects.nonNull(data) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return BUILDER.body(Result.build(status, status.getReasonPhrase(), data));
    }
}
