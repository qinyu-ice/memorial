package org.qinyu.controller;

import lombok.AllArgsConstructor;
import org.qinyu.util.Result;
import org.qinyu.dto.UserLoginDTO;
import org.qinyu.dto.UserRegisterDTO;
import org.qinyu.dto.UserResetDTO;
import org.qinyu.entity.User;
import org.qinyu.vo.SimpleUserVO;
import org.qinyu.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/user", produces = "application/json; charset=utf-8")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/register")
    public Result<Object> register(@RequestBody UserRegisterDTO dto) {
        userService.register(dto.getUname(), dto.getPasswd(), dto.getPasswd2());
        return Result.ok("用户" + dto.getUname() + "注册成功");
    }

    @PostMapping(value = "/login")
    public Result<Map<String, Object>> login(@RequestBody UserLoginDTO dto) {
        Map<String, Object> data = userService.login(dto.getUname(), dto.getPasswd());
        return Result.ok("用户" + dto.getUname() + "登录成功", data);
    }

    @PutMapping(value = "/reset")
    public Result<Object> reset(@RequestBody UserResetDTO dto) {
        userService.reset(dto.getUname(), dto.getPasswd(), dto.getPasswd2());
        return Result.ok("用户" + dto.getUname() + "重置密码成功");
    }

    @GetMapping(value = "/simple/{uid}")
    public Result<SimpleUserVO> findSimpleById(@PathVariable Integer uid) {
        User user = userService.getById(uid);
        return Result.ok("成功获取uid为" + uid + "的简要用户信息", new SimpleUserVO(user));
    }
}
