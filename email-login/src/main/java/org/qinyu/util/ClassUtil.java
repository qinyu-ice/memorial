package org.qinyu.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassUtil {

    @SuppressWarnings(value = "unchecked")
    public static <E> Class<E> getGenericClass(Class<?> clazz) {
        Type superclass = clazz.getGenericSuperclass();
        if (superclass instanceof ParameterizedType type) {
            return (Class<E>) type.getActualTypeArguments()[0];
        }
        return (Class<E>) Object.class;
    }
}
