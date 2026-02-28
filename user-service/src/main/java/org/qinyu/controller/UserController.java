package org.qinyu.controller;

import lombok.AllArgsConstructor;
import org.qinyu.dto.*;
import org.qinyu.tool.Result;
import org.qinyu.entity.User;
import org.qinyu.vo.SimpleUserVO;
import org.qinyu.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/user", produces = "application/json; charset=utf-8")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/register")
    public Result<Object> register(@RequestBody UserRegisterDTO dto) {
        userService.register(dto.getUsername(), dto.getPassword(), dto.getPassword2(),dto.getEmail());
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

    @GetMapping(value = "/all")
    public Result<List<UserDTO>> getAll() {
        List<UserDTO> userDTOList = userService.getAll();
        return Result.ok("成功获取所有用户", userDTOList);
    }

    @GetMapping(value = "/simple/{id}")
    public Result<SimpleUserVO> findSimpleById(@PathVariable Integer id) {
        User user = userService.getById(id);
        return Result.ok("成功获取uid为" + id + "的简要用户信息", new SimpleUserVO(user));
    }

    @PostMapping(value = "/add")
    public Result<Object> add(@RequestBody UserAddDTO dto) {
        userService.add(dto);
        return Result.ok("用户" + dto.getUsername() + "新增成功");
    }

    @DeleteMapping(value = "/delete/{id}")
    public Result<Object> delete(@PathVariable Integer id) {
        userService.deleteById(id);
        return Result.ok("用户" + id + "删除成功");
    }

    @PostMapping(value = "/update")
    public Result<Object> update(@RequestBody UserUpdateDTO dto) {
        userService.update(dto);
        return Result.ok("用户" + dto.getId() + "更新成功");
    }
}
