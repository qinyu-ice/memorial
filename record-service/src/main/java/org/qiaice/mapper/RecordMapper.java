package org.qiaice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.qiaice.entity.table.Record;

@Mapper
public interface RecordMapper extends BaseMapper<Record> {
}
