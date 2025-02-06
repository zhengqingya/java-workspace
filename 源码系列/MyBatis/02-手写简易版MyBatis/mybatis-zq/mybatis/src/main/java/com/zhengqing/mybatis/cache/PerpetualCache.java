package com.zhengqing.mybatis.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> 一级缓存，永久缓存 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/28 04:43
 */
public class PerpetualCache implements Cache {

    private String id;
    private Map<Object, Object> cacheMap = new HashMap<>();

    public PerpetualCache(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        this.cacheMap.put(key, value);
    }

    @Override
    public Object getObject(Object key) {
        return this.cacheMap.get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return this.cacheMap.remove(key);
    }

    @Override
    public void clear() {
        this.cacheMap.clear();
    }
}
