package org.qiaice.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.qiaice.entity.table.User;

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
