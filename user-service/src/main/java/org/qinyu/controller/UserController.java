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
        userService.register(dto.getUsername(), dto.getPassword(), dto.getPassword2());
        return Result.ok("用户" + dto.getUsername() + "注册成功");
    }

    @PostMapping(value = "/login")
    public Result<Map<String, Object>> login(@RequestBody UserLoginDTO dto) {
        Map<String, Object> data = userService.login(dto.getUsername(), dto.getPassword());
        return Result.ok("用户" + dto.getUsername() + "登录成功", data);
    }

    @PutMapping(value = "/reset")
    public Result<Object> reset(@RequestBody UserResetDTO dto) {
        userService.reset(dto.getUsername(), dto.getPassword(), dto.getPassword2());
        return Result.ok("用户" + dto.getUsername() + "重置密码成功");
    }

    @GetMapping(value = "/simple/{id}")
    public Result<SimpleUserVO> findSimpleById(@PathVariable Integer id) {
        User user = userService.getById(id);
        return Result.ok("成功获取uid为" + id + "的简要用户信息", new SimpleUserVO(user));
    }
}
