package org.qinyu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinyu.entity.Record;
import org.qinyu.vo.SimpleRecordVO;

import java.util.List;

public interface RecordService extends IService<Record> {

    List<SimpleRecordVO> findByPage(Integer page, Integer pageSize);
}
