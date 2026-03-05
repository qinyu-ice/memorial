package org.qinyu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qinyu.entity.Story;
import org.qinyu.mapper.StoryMapper;
import org.qinyu.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoryServiceImpl extends ServiceImpl<StoryMapper, Story> implements StoryService {

    @Autowired
    private StoryMapper storyMapper;

    @Override
    public void updateStoryById(Story story) {
        storyMapper.updateStoryById(story);
    }
}
