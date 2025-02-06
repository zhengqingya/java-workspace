package com.zhengqing.mybatis.executor;

import com.google.common.collect.Lists;
import com.zhengqing.mybatis.plugin.InterceptorChain;
import com.zhengqing.mybatis.plugin.LimitInterceptor;
import com.zhengqing.mybatis.plugin.SqlLogInterceptor;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * <p> 测试插件 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/26 01:12
 */
public class TestPlugin {

    @Test
    public void test() throws Exception {
        // A -> B -> C
        UserService target = new UserServiceImpl();
        LimitInterceptor limitInterceptor = new LimitInterceptor();
        target = limitInterceptor.plugin(target);
        target = new SqlLogInterceptor().plugin(target);
        System.out.println(target.selectOne("zq"));
    }

    @Test
    public void test02() throws Exception {
        List<Integer> list = Collections.unmodifiableList(Lists.newArrayList(1, 2, 3));
        list.remove(1);
    }

    @Test
    public void test03() throws Exception {
        InterceptorChain interceptorChain = new InterceptorChain();
        interceptorChain.addInterceptor(new LimitInterceptor());
//        interceptorChain.addInterceptor(new SqlInterceptor());
        UserService userService = (UserService) interceptorChain.pluginAll(new UserServiceImpl());
        System.out.println(userService.selectOne("zq"));
    }


}
