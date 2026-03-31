package org.qinyu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qinyu.entity.MartyrStory;
import org.qinyu.mapper.MartyrStoryMapper;
import org.qinyu.service.MartyrStoryService;
import org.springframework.stereotype.Service;

@Service
public class MartyrStoryServiceImpl extends ServiceImpl<MartyrStoryMapper, MartyrStory> implements MartyrStoryService {

    private MartyrStoryMapper martyrStoryMapper;

    @Override
    public void updateMartyrStoryById(MartyrStory martyrStory) {
        martyrStoryMapper.updateMartyrStoryById(martyrStory);
    }
}
