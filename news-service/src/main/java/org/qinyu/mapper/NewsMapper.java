package org.qinyu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.qinyu.entity.News;

@Mapper
public interface NewsMapper extends BaseMapper<News> {

    void updateNewsById(News news);
}
