package org.qinyu.service.client;

import org.qinyu.tool.Result;
import org.qinyu.vo.SimpleUserVO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(value = "/api/user")
public interface UserClient {

    @GetExchange(value = "/simple/{id}")
    Result<SimpleUserVO> findSimpleById(@PathVariable Integer id);
}
