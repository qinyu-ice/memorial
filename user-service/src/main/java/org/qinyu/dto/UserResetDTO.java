package org.qinyu.dto;

import lombok.Data;

@Data
public class UserResetDTO {

    private String uname;

    private String passwd;

    private String passwd2;
}
