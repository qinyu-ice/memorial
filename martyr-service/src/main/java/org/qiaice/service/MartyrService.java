package org.qiaice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qiaice.entity.dto.MartyrDTO;
import org.qiaice.entity.table.Martyr;
import org.qiaice.entity.vo.PageVO;
import org.qiaice.entity.vo.SimpleMartyrVO;
import org.qiaice.entity.vo.SimpleSmartMartyrVO;

import java.io.IOException;

public interface MartyrService extends IService<Martyr> {
    PageVO<SimpleSmartMartyrVO> smartSearch(MartyrDTO martyrDTO, Integer page, Integer pageSize) throws IOException;
}
