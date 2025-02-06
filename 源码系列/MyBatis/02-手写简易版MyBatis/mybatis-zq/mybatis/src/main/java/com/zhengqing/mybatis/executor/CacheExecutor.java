package com.zhengqing.mybatis.executor;

import com.zhengqing.mybatis.cache.Cache;
import com.zhengqing.mybatis.mapping.MappedStatement;

import java.util.List;

/**
 * <p> 缓存执行器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/28 05:31
 */
public class CacheExecutor implements Executor {

    private Executor delegate;

    public CacheExecutor(Executor delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T> List<T> query(MappedStatement ms, Object parameter) {
        Cache cache = ms.getCache();
        String cacheKey = ms.getCacheKey(parameter);
        Object cacheData = cache.getObject(cacheKey);
        if (cacheData != null) {
            return (List<T>) cacheData;
        }
        List<Object> list = this.delegate.query(ms, parameter);
        cache.putObject(cacheKey, list);
        return (List<T>) list;
    }

    @Override
    public int update(MappedStatement ms, Object parameter) {
        ms.getCache().clear();
        return this.delegate.update(ms, parameter);
    }

    @Override
    public void commit() {
        this.delegate.commit();
    }

    @Override
    public void rollback() {
        this.delegate.rollback();
    }

    @Override
    public void close() {
        this.delegate.close();
    }
}
