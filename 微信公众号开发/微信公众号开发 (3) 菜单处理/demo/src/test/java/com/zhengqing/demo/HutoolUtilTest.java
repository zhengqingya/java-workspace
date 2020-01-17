package com.zhengqing.demo;

import cn.hutool.core.date.DateTime;
import com.zhengqing.demo.utils.DateTimeUtils;

/**
 * <p> Hutool工具包方法测试$ </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/16$ 15:34$
 */
public class HutoolUtilTest {

    public static void main(String[] args) {
        DateTime now = DateTime.now();

        String s = DateTimeUtils.formatDateTime(System.currentTimeMillis()/1000);
        System.out.println(s);
    }

}
