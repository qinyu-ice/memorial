package org.qinyu.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.qinyu.entity.Record;
import org.qinyu.entity.User;
import org.qinyu.tool.Result;
import org.qinyu.vo.PageVO;
import org.qinyu.vo.SimpleRecordVO;
import org.qinyu.mapper.RecordMapper;
import org.qinyu.service.RecordService;
import org.qinyu.service.client.MartyrClient;
import org.qinyu.service.client.UserClient;
import org.qinyu.vo.SimpleUserVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    private final UserClient userClient;
    private final MartyrClient martyrClient;

    @Override
    public Page<SimpleRecordVO> findByPage(Integer page, Integer pageSize, Integer userId) {
        Page<Record> paged = lambdaQuery()
                .like(userId != 0, Record::getUserId, userId)
                .page(Page.of(page, pageSize));
        Page<SimpleRecordVO> simpleRecordVOPageVO = new Page<>();
        simpleRecordVOPageVO.setTotal(paged.getTotal());
        List<SimpleRecordVO> simpleRecordVOList = new ArrayList<>();
        paged.getRecords().forEach(record -> {
            String username = userClient.findSimpleById(record.getUserId()).data().getUsername();
            String name = martyrClient.findSimpleById(record.getMartyrId()).data().getName();
            SimpleRecordVO simpleRecordVO = new SimpleRecordVO(record.getId(), username, name, record.getIgnite(), record.getFlower(), record.getMessage(), record.getTime()
            );
            simpleRecordVOList.add(simpleRecordVO);
        });
        simpleRecordVOPageVO.setRecords(simpleRecordVOList);
        return simpleRecordVOPageVO;
    }

    @Override
    public List<SimpleRecordVO> findAll() {
        List<SimpleRecordVO> simpleRecordVOList = new ArrayList<>();
        lambdaQuery().orderByDesc(Record::getTime).list().forEach(record -> {
            SimpleRecordVO simpleRecordVO = new SimpleRecordVO();
            String username = userClient.findSimpleById(record.getUserId()).data().getUsername();
            String martyrName = martyrClient.findSimpleById(record.getMartyrId()).data().getName();
            simpleRecordVO.setId(record.getId());
            simpleRecordVO.setUsername(username);
            simpleRecordVO.setMartyrName(martyrName);
            simpleRecordVO.setIgnite(record.getIgnite());
            simpleRecordVO.setFlower(record.getFlower());
            simpleRecordVO.setMessage(record.getMessage());
            simpleRecordVO.setTime(record.getTime());
            simpleRecordVOList.add(simpleRecordVO);
        });
        return simpleRecordVOList;
    }
}
