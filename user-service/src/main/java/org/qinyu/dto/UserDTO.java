package org.qinyu.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Integer id;

    private String realName;

    private String username;

    private String email;

    private String phone;

    private Integer permission;

    private Integer isDelete;
}
