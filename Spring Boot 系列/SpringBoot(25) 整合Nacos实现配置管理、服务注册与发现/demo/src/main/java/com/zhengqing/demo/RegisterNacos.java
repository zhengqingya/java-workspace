package com.zhengqing.demo;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * <p> 注册服务 </p>
 *
 * @author : zhengqing
 * @description :  //【 Nacos Api注册服务方式 】: curl -X PUT 'http://www.zhengqingya.com:8848/nacos/v1/ns/instance?serviceName=example&ip=127.0.0.1&port=8080'
 * @date : 2020/3/30 21:18
 */
@Component
public class RegisterNacos {

    @NacosInjected
    private NamingService namingService;

    @Value("${server.port}")
    private int serverPort;

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 注册服务
     *
     * @throws NacosException
     */
    @PostConstruct // 修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次！！！
    public void registerInstance() throws NacosException {
        namingService.registerInstance(applicationName, "127.0.0.1", serverPort);
    }

}
