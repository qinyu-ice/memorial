package org.qinyu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.qinyu.entity.Record;
import org.qinyu.vo.SimpleRecordVO;

import java.util.List;

public interface RecordService extends IService<Record> {

    Page<SimpleRecordVO> findByPage(Integer page, Integer pageSize,Integer userId);

    List<SimpleRecordVO> findAll();
}
