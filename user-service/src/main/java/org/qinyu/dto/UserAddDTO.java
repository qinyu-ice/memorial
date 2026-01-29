package org.qinyu.dto;

import lombok.Data;

@Data
public class UserAddDTO {

    private String realName;

    private String username;

    private String password;

    private String email;

    private String phone;
}
