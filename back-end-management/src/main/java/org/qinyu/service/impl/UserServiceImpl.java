package org.qinyu.service.impl;

import jakarta.annotation.Resource;
import org.qinyu.entity.User;
import org.qinyu.mapper.UserMapper;
import org.qinyu.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public int add(User user) {

        return 0;
    }
}
