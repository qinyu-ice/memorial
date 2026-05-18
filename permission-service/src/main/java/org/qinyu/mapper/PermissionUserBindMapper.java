package org.qinyu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.qinyu.entity.PermissionUserBind;

import java.util.List;

@Mapper
public interface PermissionUserBindMapper extends BaseMapper<PermissionUserBind> {

    void insertBatch(List<PermissionUserBind> binds);

    void deleteBatch(List<PermissionUserBind> binds);

    List<PermissionUserBind> existBatch(List<PermissionUserBind> binds);
}
