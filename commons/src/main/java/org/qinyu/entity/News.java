package org.qinyu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class News {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String title;

    private String subtitle;

    private String source;

    private String img;

    private String content;

    // 插入时自动填充
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime time;

    // 插入时自动填充
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 插入时自动填充
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime updateTime;
}
