package com.qt.farm.utils;

import cn.hutool.core.collection.CollUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: mt
 * @description: 线程变量工具类
 * @create: 16:50 2022/7/26
 */
public class ThreadLocalUtils    {

    private static final ThreadLocal<Map<Object, Object>> MAP_THREAD_LOCAL = ThreadLocal.withInitial(HashMap::new);

    private static Map<Object, Object> cacheMap;

    static {
        cacheMap = MAP_THREAD_LOCAL.get();
    }

    public static Map<Object, Object> getThreadLocal() {
        return MAP_THREAD_LOCAL.get();
    }

    public static <T> T get(Object key) {
        if (CollUtil.isEmpty(cacheMap)) {
            return null;
        }
        return (T) cacheMap.get(key);
    }

    public static <T> T get(Object key, Object defaultValue) {
        if (CollUtil.isEmpty(cacheMap)) {
            return (T) defaultValue;
        }
        return (T) cacheMap.get(key);
    }

    public static Object set(Object key, Object value) {
        return cacheMap.put(key, value);
    }

    public static void set(Map<Object, Object> map) {
        cacheMap.putAll(map);
    }

    public static void removeValue(Object key) {
        MAP_THREAD_LOCAL.remove();
        if (!Objects.isNull(cacheMap.get(key))) {
            cacheMap.remove(key);
        }
    }
}

