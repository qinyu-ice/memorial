package org.qinyu.vo;

import lombok.Data;
import org.qinyu.entity.Martyr;

@Data
public class SimpleSmartMartyrVO {
    private Integer mid;

    private String name;

    private Double score;

    public SimpleSmartMartyrVO(Martyr martyr) {
        this.mid = martyr.getMid();
        this.name = martyr.getName();
    }
}
