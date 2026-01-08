package org.qinyu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record {

    @TableId(value = "rid", type = IdType.AUTO)
    private Integer rid;

    private Integer uid;

    private Integer mid;

    private Integer ignite;

    private Integer flower;

    private String message;

    private LocalDateTime time;
}
