package org.qinyu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qinyu.dto.UserAddDTO;
import org.qinyu.dto.UserDTO;
import org.qinyu.dto.UserUpdateDTO;
import org.qinyu.entity.User;
import org.qinyu.expcetion.CustomException;
import org.qinyu.mapper.UserMapper;
import org.qinyu.service.UserService;
import org.qinyu.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder encoder;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void register(String username, String password, String password2, String email) {
        if (Objects.equals(username, "") || Objects.equals(password, "") || Objects.equals(password2, "") || Objects.equals(email, "")) {
            throw new CustomException("数据不能为空字符串");
        }
        if (!password.equals(password2)) throw new CustomException("前后两段密码不一致");
        User record = lambdaQuery().eq(User::getUsername, username).one();
        if (record != null) throw new CustomException("用户名" + username + "已存在");
        User user = lambdaQuery().eq(User::getEmail, email).one();
        if (user != null) throw new CustomException("邮箱" + email + "已存在");
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        String emailPassword = passwordEncoder.encode("123456");
        if (!save(new User(null, null, username, password, email, emailPassword, null, null, null, LocalDateTime.now(), LocalDateTime.now())))
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
        token.put("realName", record.getRealName());
        token.put("email", record.getEmail());
        token.put("phone", record.getPhone());
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
        if (lambdaUpdate().eq(User::getUsername, username).set(User::getPassword, password2).update()) {
            // 当密码重置成功时，更新更新时间
            lambdaUpdate().eq(User::getUsername, username).set(User::getUpdateTime, LocalDateTime.now()).update();
        } else {
            throw new CustomException("重置用户密码失败");
        }
    }

    @Override
    public List<UserDTO> getAll() {
        List<UserDTO> userDTOList = userMapper.getAll();
        if (userDTOList.isEmpty()) {
            throw new CustomException("暂无用户");
        }
        return userDTOList;
    }

    @Override
    public void add(UserAddDTO dto) {
        User record = lambdaQuery().eq(User::getUsername, dto.getUsername()).one();
        if (record != null) throw new CustomException("用户名" + dto.getUsername() + "已被占用");
        String password = DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8));
        String emailPassword = passwordEncoder.encode("123456");
        if (!save(new User(null, dto.getRealName(), dto.getUsername(), password, dto.getEmail(), emailPassword, dto.getPhone(), 2, 0, LocalDateTime.now(), LocalDateTime.now())))
            throw new CustomException("用户新增失败");
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) throw new CustomException("id不能为空");
        userMapper.deleteById(id);
    }

    @Override
    public void update(UserUpdateDTO dto) {
        if (dto.getPassword() != null) {
            dto.setPassword(DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8)));
        }
        if (dto.getEmailPassword() != null) {
            dto.setEmailPassword(encoder.encode(dto.getEmailPassword()));
        }
        userMapper.updateById(dto);
    }
}
