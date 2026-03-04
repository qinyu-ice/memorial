package org.qinyu.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 字段自动填充处理器
 */
@Component // 必须交给Spring容器管理，否则无法生效
public class AutoFillTimeConfig implements MetaObjectHandler {

    /**
     * 插入操作时的字段填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 填充createTime：插入时设置为当前时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        // 填充updateTime：插入时也设置为当前时间
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    /**
     * 更新操作时的字段填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 填充updateTime：更新时设置为当前时间
//        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}