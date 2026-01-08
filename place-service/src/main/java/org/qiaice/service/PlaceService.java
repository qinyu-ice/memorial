package org.qiaice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qiaice.entity.table.Place;

import java.io.IOException;

public interface PlaceService extends IService<Place> {
    String getAddressByIp(String ip) throws IOException;
}
