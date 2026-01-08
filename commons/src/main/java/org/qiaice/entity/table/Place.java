package org.qiaice.entity.table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Place {

    @TableId(value = "pid", type = IdType.AUTO)
    private Integer pid;

    private String name;

    private String img;

    private String intro;

    private String phone;

    private String address;

    private String hint;
}
