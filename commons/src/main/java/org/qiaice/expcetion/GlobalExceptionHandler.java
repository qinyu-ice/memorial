package org.qiaice.expcetion;

import lombok.extern.slf4j.Slf4j;
import org.qiaice.entity.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<Object> exceptionHandler(Exception ex){
        if (ex instanceof CustomException ce) return Result.no(ce.getMessage());
        log.error("程序内部出现严重错误, 请尽早修复! ", ex);
        return Result.no("服务器发生致命错误, 正在努力修复中...");
    }
}
