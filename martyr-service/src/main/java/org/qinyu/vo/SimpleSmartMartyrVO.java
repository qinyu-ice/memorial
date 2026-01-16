package org.qinyu.vo;

import lombok.Data;
import org.qinyu.entity.Martyr;

@Data
public class SimpleSmartMartyrVO {
    private Integer id;

    private String name;

    private Double score;

    public SimpleSmartMartyrVO(Martyr martyr) {
        this.id = martyr.getId();
        this.name = martyr.getName();
    }
}
