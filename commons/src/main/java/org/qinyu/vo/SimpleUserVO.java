package org.qinyu.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.qinyu.entity.User;

@Data
@NoArgsConstructor
public class SimpleUserVO {

    private Integer id;

    private String username;

    public SimpleUserVO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}
