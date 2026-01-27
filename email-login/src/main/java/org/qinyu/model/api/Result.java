package org.qinyu.model.api;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.util.Objects;

public record Result<T>(Integer code, String message, T data, Long timestamp) {
    private static final Object NULL = new Object();

    @SuppressWarnings(value = "unchecked")
    public static <T> Result<T> build(HttpStatus status, String message, T data) {
        return new Result<>(status.value(), StringUtils.hasText(message) ? message : status.getReasonPhrase(),
                Objects.nonNull(data) ? data : (T) NULL, System.currentTimeMillis());
    }
}
