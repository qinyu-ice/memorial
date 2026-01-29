package org.qinyu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qinyu.dto.UserAddDTO;
import org.qinyu.entity.User;
import org.qinyu.expcetion.CustomException;
import org.qinyu.mapper.UserMapper;
import org.qinyu.service.UserService;
import org.qinyu.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void register(String username, String password, String password2) {
        if (!password.equals(password2)) throw new CustomException("前后两段密码不一致");
        User record = lambdaQuery().eq(User::getUsername, username).one();
        if (record != null) throw new CustomException("用户名" + username + "已被占用");
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!save(new User(null, username, password, null, null, null, null, null)))
            throw new CustomException("用户注册失败");
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        User record = lambdaQuery().eq(User::getUsername, username).one();
        if (record == null) throw new CustomException("用户" + username + "未注册");
        if (record.getIsDelete() == 1) throw new CustomException("用户" + username + "已删除");
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!record.getPassword().equals(password))
            throw new CustomException("用户名或密码错误");
        Map<String, Object> token = new HashMap<>(TokenUtil.createToken(record));
        token.put("is_delete", record.getIsDelete());
        token.put("permission", record.getPermission());
        redisTemplate.opsForValue().set("token", token.get("token"), Duration.ofHours(2));
        redisTemplate.opsForValue().set("refresh", "refresh" + token.get("token"), Duration.ofDays(7));
        return token;
    }

    @Override
    public void reset(String username, String password, String password2) {
        User record = lambdaQuery().eq(User::getUsername, username).one();
        if (record == null) throw new CustomException("用户" + username + "未注册");
        String tmpPassword = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!record.getPassword().equals(tmpPassword))
            throw new CustomException("用户名或密码错误");
        if (password.equals(password2)) throw new CustomException("新旧密码不能一致");
        password2 = DigestUtils.md5DigestAsHex(password2.getBytes(StandardCharsets.UTF_8));
        if (!lambdaUpdate().eq(User::getUsername, username).set(User::getPassword, password2).update())
            throw new CustomException("重置用户密码失败");
    }

    @Override
    public void add(UserAddDTO dto){
        User record = lambdaQuery().eq(User::getUsername, dto.getUsername()).one();
        if (record != null) throw new CustomException("用户名" + dto.getUsername() + "已被占用");
        String password = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!save(new User(null, dto.getUsername(), password, dto.getRealName(), dto.getEmail(), dto.getPhone(), null, null)))
            throw new CustomException("用户新增失败");
    }
}
