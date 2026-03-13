package org.qinyu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinyu.entity.News;

public interface NewsService extends IService<News> {

    void updateNewsById(News news);
}
