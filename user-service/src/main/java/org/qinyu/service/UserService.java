package org.qinyu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinyu.entity.User;

import java.util.Map;

public interface UserService extends IService<User> {

    void register(String username, String password, String password2);

    Map<String, Object> login(String username, String password);

    void reset(String username, String password, String password2);
}
