package org.qinyu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Martyr {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String photo;

    private String name;

    private String hometown;

    private Integer gender;

    private String politicsStatus;

    private String dept;

    private String position;

    private String achievement;

    private String deathDate;

    private String deathCampaign;

    private String deathAddress;

    private String buryPoint;

    private String deeds;
}
