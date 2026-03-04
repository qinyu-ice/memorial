package org.qinyu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinyu.dto.MartyrDTO;
import org.qinyu.entity.Martyr;
import org.qinyu.vo.PageVO;
import org.qinyu.vo.SimpleSmartMartyrVO;

import java.io.IOException;

public interface MartyrService extends IService<Martyr> {
    PageVO<SimpleSmartMartyrVO> smartSearch(MartyrDTO martyrDTO, Integer page, Integer pageSize) throws IOException;

    void updateMartyrById(Martyr martyr);
}
