package org.qinyu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinyu.entity.User;

import java.util.Map;

public interface UserService extends IService<User> {

    void register(String uname, String passwd, String passwd2);

    Map<String, Object> login(String uname, String passwd);

    void reset(String uname, String passwd, String passwd2);
}
