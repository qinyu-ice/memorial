package org.qinyu.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.qinyu.entity.User;

@Data
@NoArgsConstructor
public class SimpleUserVO {

    private Integer uid;

    private String uname;

    public SimpleUserVO(User user) {
        this.uid = user.getUid();
        this.uname = user.getUname();
    }
}
