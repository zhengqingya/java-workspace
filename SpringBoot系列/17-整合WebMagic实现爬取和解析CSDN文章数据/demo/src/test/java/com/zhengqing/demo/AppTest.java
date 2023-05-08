package com.zhengqing.demo;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * <p>
 * 应用测试$
 * </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/6/2$ 18:53$
 */
public class AppTest {

    @Test
    public void testParse() throws Exception {
        try {
            Document document = Jsoup.connect("https://www.yabovip2009.com/app/aviaGaming").get();
            System.out.println(document.title());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
