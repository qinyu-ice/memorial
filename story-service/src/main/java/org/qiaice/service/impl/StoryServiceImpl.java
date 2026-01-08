package org.qiaice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qiaice.entity.table.Story;
import org.qiaice.mapper.StoryMapper;
import org.qiaice.service.StoryService;
import org.springframework.stereotype.Service;

@Service
public class StoryServiceImpl extends ServiceImpl<StoryMapper, Story> implements StoryService {
}
