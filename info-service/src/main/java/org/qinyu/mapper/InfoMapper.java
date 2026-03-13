package org.qinyu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.qinyu.entity.Info;

@Mapper
public interface InfoMapper extends BaseMapper<Info> {

    void updateInfoById(Info info);
}
