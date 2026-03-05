package org.qinyu.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SimpleRecordVO {

    private Integer id;

    private String username;

    private String martyrName;

    private Integer ignite;

    private Integer flower;

    private String message;

    private LocalDateTime time;

    public SimpleRecordVO() {

    }
}
