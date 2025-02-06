package com.zhengqing.mybatis.design.proxy._01;

import org.junit.Test;

/**
 * <p> 测试代理模式 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/21 17:12
 */
public class TestProxy {

    @Test
    public void test() throws Exception {
        UserProxy userProxy = new UserProxy(new UserServiceImpl());
        Object xx = userProxy.selectList("xx");
        System.out.println(xx);
    }

}
