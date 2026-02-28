package org.qinyu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String realName;

    private String username;

    private String password;

    private String email;

    private String emailPassword;

    private String phone;

    private Integer permission;

    private Integer isDelete;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
