package org.qinyu.vo;

import lombok.Data;
import org.qinyu.entity.Place;

import java.time.LocalDateTime;

@Data
public class SimplePlaceVO {

    private Integer id;

    private String name;

    private String img;

    private String longitude;

    private String latitude;

    private String introduction;

    private String phone;

    private String address;

    private String hint;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public SimplePlaceVO(Place place) {
        this.id = place.getId();
        this.name = place.getName();
        this.img = "https://memorial-dazhou.oss-cn-chengdu.aliyuncs.com" + place.getImg();
        this.longitude = place.getLongitude();
        this.latitude = place.getLatitude();
        this.introduction = place.getIntroduction();
        this.phone = place.getPhone();
        this.address = place.getAddress();
        this.hint = place.getHint();
        this.createTime = place.getCreateTime();
        this.updateTime = place.getUpdateTime();
    }
}
