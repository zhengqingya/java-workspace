package com.zhengqing.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

@Slf4j
public class _08_test_trigger_time {

    @Before
    public void junit4_setUp() {
        // 初始化代码
        System.out.println("JUnit 4: setUp 方法执行");
    }


    @Test
    public void test_junit4_method1() {
        System.out.println("JUnit 4: method1 执行");
    }

    @Test
    public void test_junit4_method2() {
        System.out.println("JUnit 4: method2 执行");
    }

    // --------------------------------------------

    @BeforeEach
    public void junit5_setUp() {
        // 初始化代码
        System.out.println("JUnit 5: setUp 方法执行");
    }

    @org.junit.jupiter.api.Test
    public void test_junit5_method1() {
        System.out.println("JUnit 5: method1 执行");
    }

    @org.junit.jupiter.api.Test
    public void test_junit5_method2() {
        System.out.println("JUnit 5: method2 执行");
    }

}
