package org.qinyu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinyu.entity.Story;

public interface StoryService extends IService<Story> {

    void updateStoryById(Story story);
}
