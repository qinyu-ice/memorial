package org.qinyu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Place {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String img;

    private String introduction;

    private String phone;

    private String address;

    private String hint;

    // 插入时自动填充
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 插入时自动填充
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime updateTime;
}
