package org.qinyu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinyu.entity.MartyrStory;

public interface MartyrStoryService extends IService<MartyrStory> {

    void updateMartyrStoryById(MartyrStory martyrStory);
}
