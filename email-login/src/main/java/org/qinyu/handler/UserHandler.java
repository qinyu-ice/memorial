package org.qinyu.handler;

import lombok.RequiredArgsConstructor;
import org.qinyu.handler.exception.BaseException;
import org.qinyu.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Component
@RequiredArgsConstructor
public class UserHandler extends BaseHandler {
    private final UserService service;

    public ServerResponse findById(ServerRequest request) {
        try {
            Integer id = Integer.valueOf(request.pathVariable("id"));
            return ok(service.findById(id));
        } catch (NumberFormatException exception) {
            throw new BaseException("uid 格式有误, 其中存在非数字字符");
        }
    }
}
