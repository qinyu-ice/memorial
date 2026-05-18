package org.qinyu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.qinyu.entity.Permission;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("select name, status, description from permission where name = #{name}")
    Permission selectByName(String name);

    Boolean deleteBatch(List<String> ids);
}
