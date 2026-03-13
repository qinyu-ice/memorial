package org.qinyu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qinyu.entity.Info;
import org.qinyu.mapper.InfoMapper;
import org.qinyu.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoServiceImpl extends ServiceImpl<InfoMapper, Info>  implements InfoService {

    @Autowired
    private InfoMapper infoMapper;

    @Override
    public void updateInfoById(Info info) {
        infoMapper.updateInfoById(info);
    }
}
