package org.qinyu.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {

    private Integer id;

    private String realName;

    private String username;

    private String password;

    private String email;

    private String emailPassword;

    private String phone;

    private Integer permission;
}
