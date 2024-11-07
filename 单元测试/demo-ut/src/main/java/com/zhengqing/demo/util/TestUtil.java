package com.zhengqing.demo.util;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestUtil {

    public static void doNothing() {
        System.out.println("[静态方法-无返回值] TestUtil.doNothing...");
    }

    public static String getNowTime(Integer num) {
        System.out.println("[静态方法-带返回值] TestUtil.getNowTime..." + num);
        return DateUtil.now();
    }
}
