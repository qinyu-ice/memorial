package org.qinyu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.qinyu.dto.UserUpdateDTO;
import org.qinyu.entity.User;
import org.qinyu.vo.SimpleUserVO;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Update("update user set is_delete = 1, update_time = now() where id = #{id}")
    void deleteById(@Param("id") Integer id);

    void updateById(UserUpdateDTO dto);

    @Select("select id, username from user where username = #{username}")
    SimpleUserVO getByUsername(String username);
}
