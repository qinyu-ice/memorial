package org.qinyu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinyu.dto.UserAddDTO;
import org.qinyu.dto.UserDTO;
import org.qinyu.dto.UserUpdateDTO;
import org.qinyu.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {

    void register(String username, String password, String password2,String email);

    Map<String, Object> login(String username, String password);

    void reset(String username, String password, String password2);

    List<UserDTO> getAll();

    void add(UserAddDTO dto);

    void deleteById(Integer id);

    void update(UserUpdateDTO dto);
}
