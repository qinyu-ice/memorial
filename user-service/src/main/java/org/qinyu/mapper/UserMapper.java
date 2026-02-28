package org.qinyu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.qinyu.dto.UserDTO;
import org.qinyu.dto.UserUpdateDTO;
import org.qinyu.entity.User;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select id, real_name, username, email, phone, permission, is_delete from user where permission = 2")
    List<UserDTO> getAll();

    @Update("update user set is_delete = 1, update_time = now() where id = #{id}")
    void deleteById(@Param("id") Integer id);

    void updateById(UserUpdateDTO dto);
}
