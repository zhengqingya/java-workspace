package com.zhengqing.mybatis.spring.mapper;

import com.zhengqing.mybatis.session.SqlSession;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p> mapper工厂bean </p>
 *
 * @author zhengqingya
 * @description 用于自定义创建bean
 * @date 2024/5/5 05:04
 */
public class MapperFactoryBean<T> implements FactoryBean<T> {

    private Class<T> mapperInterface;

    public MapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @Autowired
    private SqlSession sqlSession;

    @Override
    public T getObject() throws Exception {
        return this.sqlSession.getMapper(this.mapperInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return this.mapperInterface;
    }
}
