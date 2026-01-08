package org.qiaice.service.client;

import org.qiaice.entity.Result;
import org.qiaice.entity.vo.SimpleUserVO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(value = "/api/user")
public interface UserClient {

    @GetExchange(value = "/simple/{uid}")
    Result<SimpleUserVO> findSimpleById(@PathVariable Integer uid);
}
