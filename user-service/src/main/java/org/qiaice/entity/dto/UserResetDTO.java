package org.qiaice.entity.dto;

import lombok.Data;

@Data
public class UserResetDTO {

    private String uname;

    private String passwd;

    private String passwd2;
}
