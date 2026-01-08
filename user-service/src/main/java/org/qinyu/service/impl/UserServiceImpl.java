package org.qinyu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qinyu.entity.User;
import org.qinyu.expcetion.CustomException;
import org.qinyu.mapper.UserMapper;
import org.qinyu.service.UserService;
import org.qinyu.tool.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void register(String uname, String passwd, String passwd2) {
        if (!passwd.equals(passwd2)) throw new CustomException("前后两段密码不一致");
        User record = lambdaQuery().eq(User::getUname, uname).one();
        if (record != null) throw new CustomException("用户名" + uname + "已被占用");
        passwd = DigestUtils.md5DigestAsHex(passwd.getBytes(StandardCharsets.UTF_8));
        if (!save(new User(null, uname, passwd))) throw new CustomException("用户注册失败");

    }

    @Override
    public Map<String, Object> login(String uname, String passwd) {
        User record = lambdaQuery().eq(User::getUname, uname).one();
        if (record == null) throw new CustomException("用户" + uname + "未注册");
        passwd = DigestUtils.md5DigestAsHex(passwd.getBytes(StandardCharsets.UTF_8));
        if (!record.getPasswd().equals(passwd))
            throw new CustomException("用户名或密码错误");
        Map<String, Object> token = TokenUtil.createToken(record);
        redisTemplate.opsForValue().set("token" , token.get("token"), Duration.ofMinutes(30));
        redisTemplate.opsForValue().set("refresh" , "refresh"+token.get("token"), Duration.ofDays(7));
        return token;
    }

    @Override
    public void reset(String uname, String passwd, String passwd2) {
        User record = lambdaQuery().eq(User::getUname, uname).one();
        if (record == null) throw new CustomException("用户" + uname + "未注册");
        String tmpPasswd = DigestUtils.md5DigestAsHex(passwd.getBytes(StandardCharsets.UTF_8));
        if (!record.getPasswd().equals(tmpPasswd))
            throw new CustomException("用户名或密码错误");
        if (passwd.equals(passwd2)) throw new CustomException("新旧密码不能一致");
        passwd2 = DigestUtils.md5DigestAsHex(passwd2.getBytes(StandardCharsets.UTF_8));
        if (!lambdaUpdate().eq(User::getUname, uname).set(User::getPasswd, passwd2).update())
            throw new CustomException("重置用户密码失败");
    }
}
