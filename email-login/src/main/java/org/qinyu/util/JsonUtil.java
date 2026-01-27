package org.qinyu.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class JsonUtil {
    private static JsonMapper mapper;

    @Autowired
    public void setJsonMapper(JsonMapper mapper) {
        JsonUtil.mapper = mapper;
    }

    public static <T> String generate(T data) {
        return mapper.writeValueAsString(data);
    }

    public static <T> T parse(String json, Class<T> clazz) {
        return mapper.readValue(json, clazz);
    }
}
