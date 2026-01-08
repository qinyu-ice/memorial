package org.qiaice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qiaice.entity.table.Record;
import org.qiaice.entity.vo.SimpleRecordVO;

import java.util.List;

public interface RecordService extends IService<Record> {

    List<SimpleRecordVO> findByPage(Integer page, Integer pageSize);
}
