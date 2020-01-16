package com.zhengqing.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * <p> RestTemplate是Spring提供的用于访问Rest服务的客户端,RestTemplate提供了多种便捷访问远程Http服务的方法,能够大大提高客户端的编写效率。 </p>
 *
 * @author : zhengqing
 * @description :  `http://gturnquist-quoters.cfapps.io/api/random` -> 返回随机json字符串
 * @date : 2020/1/2 16:45
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class RestTemplateTest {

    @Test
    public void test01() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String jsonStr = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", String.class);
        log.info("随机json字符串 ： " + jsonStr);
    }

}
