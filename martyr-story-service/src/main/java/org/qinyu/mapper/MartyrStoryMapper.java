package org.qinyu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.qinyu.entity.MartyrStory;

@Mapper
public interface MartyrStoryMapper extends BaseMapper<MartyrStory> {

    void updateMartyrStoryById(MartyrStory martyrStory);
}
