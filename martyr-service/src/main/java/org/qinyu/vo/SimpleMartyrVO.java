package org.qinyu.vo;

import lombok.Data;
import org.qinyu.entity.Martyr;

@Data
public class SimpleMartyrVO {

    private Integer id;

    private String name;

    public SimpleMartyrVO(Martyr martyr) {
        this.id = martyr.getId();
        this.name = martyr.getName();
    }
}
