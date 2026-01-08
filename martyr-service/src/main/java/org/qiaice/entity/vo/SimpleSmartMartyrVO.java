package org.qiaice.entity.vo;

import lombok.Data;
import org.qiaice.entity.table.Martyr;

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
