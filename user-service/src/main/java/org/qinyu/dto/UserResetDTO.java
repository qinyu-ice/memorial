package org.qinyu.dto;

import lombok.Data;

@Data
public class UserResetDTO {

    private String username;

    private String password;

    private String password2;
}
