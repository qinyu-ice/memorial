package org.qinyu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.qinyu.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select count(*) from user")
    int selectCount();
}
