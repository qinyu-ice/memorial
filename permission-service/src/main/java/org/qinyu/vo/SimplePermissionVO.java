package org.qinyu.vo;

import lombok.Data;
import org.qinyu.entity.Permission;

import java.time.LocalDateTime;

@Data
public class SimplePermissionVO {

    private Integer id;

    private String name;

    private Integer status;

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public SimplePermissionVO(Permission permission) {
        this.id = permission.getId();
        this.name = permission.getName();
        this.status = permission.getStatus();
        this.description = permission.getDescription();
        this.createTime = permission.getCreateTime();
        this.updateTime = permission.getUpdateTime();
    }
}
