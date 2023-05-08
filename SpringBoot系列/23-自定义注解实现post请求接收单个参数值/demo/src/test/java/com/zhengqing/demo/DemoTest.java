package com.zhengqing.demo;

import java.io.InputStream;
import java.net.URL;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 小测试$
 * </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/7/10$ 11:02$
 */
@Slf4j
public class DemoTest {

    /**
     * 判断一个URL是否有效
     *
     * @throws Exception
     */
    @Test
    public void testConnectUrl() throws Exception {
        URL url;
        try {
            url = new URL("http://www.baidu.com");
            InputStream in = url.openStream();
            System.out.println("连接可用");
        } catch (Exception e1) {
            System.out.println("连接打不bai开!");
            url = null;
        }
    }

}
