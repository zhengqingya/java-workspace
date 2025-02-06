package com.zhengqing.mybatis.datasource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * <p> 代理连接 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/27 19:19
 */
public class PooledConnection implements InvocationHandler {

    private Connection connection;
    private Connection proxyConnection;
    private PooledDataSource pooledDataSource;

    public PooledConnection(PooledDataSource pooledDataSource, Connection connection) {
        this.pooledDataSource = pooledDataSource;
        this.connection = connection;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("close")) {
            this.pooledDataSource.returnConnection(this.proxyConnection);
        } else {
            return method.invoke(this.connection, args);
        }
        return null;
    }

    public Connection getProxy() {
        Connection proxy = (Connection) Proxy.newProxyInstance(this.connection.getClass().getClassLoader(), this.connection.getClass().getInterfaces(), this);
        this.proxyConnection = proxy;
        return proxy;
    }

}
