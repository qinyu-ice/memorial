package org.qinyu.vo;

import lombok.Data;
import org.qinyu.entity.Place;

@Data
public class SimplePlaceVO {

    private Integer id;

    private String name;

    private String img;

    public SimplePlaceVO(Place place) {
        this.id = place.getId();
        this.name = place.getName();
        this.img = "https://www.sctyjrsw.com/image" + place.getImg();
    }
}
