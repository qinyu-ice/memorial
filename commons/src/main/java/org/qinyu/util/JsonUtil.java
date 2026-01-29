package org.qinyu.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonUtil {

    private JsonUtil() {
    }

    private static ObjectMapper mapper;

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        JsonUtil.mapper = mapper;
    }

    /**
     * 将对象转换成 json 字符串
     *
     * @param obj 对象
     * @return json 字符串
     * @param <T> 泛型，将各种类型的对象转换成 json 字符串
     */
    public static <T> String toJson(T obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将数据 json 化并写入响应流
     *
     * @param response 响应
     * @param data 数据
     * @param <T> 泛型，将各种类型的 json 化数据写入响应流
     */
    public static <T> void write(HttpServletResponse response, T data) {
        try {
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(toJson(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
