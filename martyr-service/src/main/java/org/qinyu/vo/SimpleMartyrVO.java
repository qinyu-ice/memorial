package org.qinyu.vo;

import lombok.Data;
import org.qinyu.entity.Martyr;

@Data
public class SimpleMartyrVO {

    private Integer mid;

    private String name;

    public SimpleMartyrVO(Martyr martyr) {
        this.mid = martyr.getMid();
        this.name = martyr.getName();
    }
}
