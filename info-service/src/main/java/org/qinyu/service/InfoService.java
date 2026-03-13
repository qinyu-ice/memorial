package org.qinyu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinyu.entity.Info;

public interface InfoService extends IService<Info> {

    void updateInfoById(Info info);
}
