package org.qinyu.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.qinyu.entity.Record;
import org.qinyu.vo.SimpleRecordVO;
import org.qinyu.mapper.RecordMapper;
import org.qinyu.service.RecordService;
import org.qinyu.service.client.MartyrClient;
import org.qinyu.service.client.UserClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    private final UserClient userClient;
    private final MartyrClient martyrClient;

    @Override
    public List<SimpleRecordVO> findByPage(Integer page, Integer pageSize) {
        Page<Record> paged = lambdaQuery().orderByDesc(Record::getTime).page(Page.of(page, pageSize));
        return paged.getRecords().stream().map(record -> {
            String uname = userClient.findSimpleById(record.getUid()).data().getUname();
            String name = martyrClient.findSimpleById(record.getMid()).data().getName();
            String message = record.getMessage();
            message = message.length() > 23 ? message.substring(0, 23) + "......" : message;
            return new SimpleRecordVO(record.getRid(), uname, name, message, record.getTime());
        }).toList();
    }
}
