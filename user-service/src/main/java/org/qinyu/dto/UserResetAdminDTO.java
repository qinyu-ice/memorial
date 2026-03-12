package org.qinyu.dto;

import lombok.Data;

@Data
public class UserResetAdminDTO {

    private String username;

    private String newPassword;

    private String newEmailPassword;
}
