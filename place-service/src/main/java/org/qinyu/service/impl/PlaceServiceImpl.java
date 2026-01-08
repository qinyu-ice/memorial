package org.qinyu.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.qinyu.entity.Place;
import org.qinyu.vo.AddressVO;
import org.qinyu.mapper.PlaceMapper;
import org.qinyu.service.PlaceService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PlaceServiceImpl extends ServiceImpl<PlaceMapper, Place> implements PlaceService {
    @Override
    public String getAddressByIp(String ip) throws IOException {
        String key="43b4c8b855538aa8afb4f7a4e2f0be60";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = "https://restapi.amap.com/v3/ip?ip="+ip+"&output=json&key="+key;
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-Type", "application/json");
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        //处理响应
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        System.out.println("服务器返回的状态码为"+statusCode);
        //获取响应实体
        HttpEntity entity = httpResponse.getEntity();
        String body = EntityUtils.toString(entity);
        AddressVO addressVO = JSON.parseObject(body, AddressVO.class);
        return addressVO.getRectangle().split(";")[0];
    }
}
