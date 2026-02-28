package org.qinyu.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {

    private String username;

    private String password;

    private String password2;

    private String email;
}
