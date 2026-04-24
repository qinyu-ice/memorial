package org.qinyu.dto;

import lombok.Data;

@Data
public class UserResetEmailDTO {

    private String username;

    private String emailPassword;

    private String emailPassword2;
}
