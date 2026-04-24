package org.qinyu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinyu.dto.UserAddDTO;
import org.qinyu.dto.UserUpdateDTO;
import org.qinyu.entity.User;
import org.qinyu.vo.SimpleUserVO;

import java.util.Map;

public interface UserService extends IService<User> {

    void register(String username, String password, String password2, String email);

    Map<String, Object> login(String username, String password);

    void reset(String username, String password, String password2);

    void resetEmail(String username, String emailPassword, String emailPassword2);

    void resetAdmin(String username, String newPassword, String newEmailPassword);

    void add(UserAddDTO dto);

    void deleteById(Integer id);

    void update(UserUpdateDTO dto);

    SimpleUserVO getByUsername(String username);
}
