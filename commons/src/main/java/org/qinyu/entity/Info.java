package org.qinyu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Info {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String title;

    private String source;

    private LocalDateTime time;

    private String name;

    private Integer gender;

    private String hometown;

    private String birthDate;

    private String politicsStatus;

    private String dept;

    private String deathDate;

    private String deathCampaign;

    private String deathAddress;

    private String other;

    private String contactName;

    private String contactAddress;

    private String contactPhone;

    private String contactEmail;

    // 插入时自动填充
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 插入时自动填充
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime updateTime;
}
