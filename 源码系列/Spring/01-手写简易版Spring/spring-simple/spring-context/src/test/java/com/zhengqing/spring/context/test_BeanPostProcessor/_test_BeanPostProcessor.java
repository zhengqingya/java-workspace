package com.zhengqing.spring.context.test_BeanPostProcessor;

import com.zhengqing.spring.context.AnnotationConfigApplicationContext;
import com.zhengqing.spring.context.test_BeanPostProcessor.scan.ScanApplication;
import com.zhengqing.spring.context.test_BeanPostProcessor.scan.proxy.InjectProxyOnConstructorBean;
import com.zhengqing.spring.context.test_BeanPostProcessor.scan.proxy.OriginBean;
import com.zhengqing.spring.context.test_BeanPostProcessor.scan.proxy.SecondProxyBean;
import com.zhengqing.spring.io.PropertyResolver;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

public class _test_BeanPostProcessor {

    @Test
    public void test() throws Exception {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ScanApplication.class, createPropertyResolver());

        // 获取OriginBean的实例,此处获取的应该是SecondProxyBean
        OriginBean proxy = ctx.getBean(OriginBean.class);
        assertSame(SecondProxyBean.class, proxy.getClass());

        // proxy的字段并没有被注入
        assertNull(proxy.username);
        assertNull(proxy.password);

        // 但是调用proxy的getUsername()会最终调用原始Bean的getUsername(),从而返回正确的值
        assertEquals("root", proxy.getUsername());

        // 获取InjectProxyOnConstructorBean实例
        InjectProxyOnConstructorBean inject = ctx.getBean(InjectProxyOnConstructorBean.class);
        // 注入的OriginBean应该为Proxy，而且和前面返回的proxy是同一实例
        assertSame(proxy, inject.injected);
    }

    PropertyResolver createPropertyResolver() {
        Properties ps = new Properties();
        ps.put("jdbc.username", "root");
        ps.put("jdbc.password", "123456");
        PropertyResolver pr = new PropertyResolver(ps);
        return pr;
    }

}
