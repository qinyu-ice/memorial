package org.qinyu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinyu.entity.Place;

import java.io.IOException;

public interface PlaceService extends IService<Place> {
    String getAddressByIp(String ip) throws IOException;
}
