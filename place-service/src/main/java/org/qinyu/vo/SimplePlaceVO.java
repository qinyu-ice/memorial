package org.qinyu.vo;

import lombok.Data;
import org.qinyu.entity.Place;

@Data
public class SimplePlaceVO {

    private Integer pid;

    private String name;

    private String img;

    public SimplePlaceVO(Place place) {
        this.pid = place.getPid();
        this.name = place.getName();
        this.img = "https://www.sctyjrsw.com/image" + place.getImg();
    }
}
