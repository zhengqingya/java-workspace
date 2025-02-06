package com.zhengqing.mybatis;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * <p> debug </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/23 14:58
 */
public class TestDebug {

    private String name;
    private Integer age;

    @Test
    public void test() throws Exception {
        System.out.println(1);
        this.test_01();
        System.out.println(2);
    }

    private void test_01() {
        System.out.println(111);
        this.test_02();
        System.out.println(222);
        System.out.println(this.name);
    }

    private void test_02() {
        System.out.println(333);
        this.name = RandomUtil.randomString(5);
        System.out.println(444);
    }

    @Test
    public void test02() throws Exception {
        System.out.println(Lists.newArrayList(1, 2, 3, 4, 5, 6)
                .stream()
                .filter(i -> i % 2 == 0)
                .max(Integer::compareTo)
                .get());
    }

    @Test
    public void testThread() throws Exception {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " xxx ");
                System.out.println("11111");
            }, "线程" + i).start();
        }

        System.out.println("主线程");
        ThreadUtil.sleep(30, TimeUnit.SECONDS);
    }


}
