package org.qinyu.service.client;

import org.qinyu.tool.Result;
import org.qinyu.vo.SimpleMartyrVO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(value = "/api/martyr")
public interface MartyrClient {

    @GetExchange(value = "/simple/{id}")
    Result<SimpleMartyrVO> findSimpleById(@PathVariable Integer id);
}
