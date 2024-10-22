package com.zhengqing.spring.aop._02;

import com.zhengqing.spring.context.AnnotationConfigApplicationContext;
import com.zhengqing.spring.io.PropertyResolver;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

@Slf4j
public class TestAround {

    @Test
    public void test() {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AroundApplication.class, createPropertyResolver())) {
            OriginBean proxy = ctx.getBean(OriginBean.class);
            System.out.println(proxy.getClass().getName());

            // Proxy类与OriginBean.class不同
            assertNotSame(OriginBean.class, proxy.getClass());
            // proxy实例的name字段应为null
            assertNull(proxy.name);

            System.out.println(proxy.hello());
            System.out.println(proxy.hi());

            // test injected proxy:
            OtherBean other = ctx.getBean(OtherBean.class);
            assertSame(proxy, other.origin);
            System.out.println(other.origin.hello());
        }
    }

    PropertyResolver createPropertyResolver() {
        Properties ps = new Properties();
        ps.put("jdbc.username", "root");
        ps.put("jdbc.password", "123456");
        PropertyResolver pr = new PropertyResolver(ps);
        return pr;
    }

}
