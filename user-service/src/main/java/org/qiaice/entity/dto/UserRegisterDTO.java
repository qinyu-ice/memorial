package org.qiaice.entity.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {

    private String uname;

    private String passwd;

    private String passwd2;
}
