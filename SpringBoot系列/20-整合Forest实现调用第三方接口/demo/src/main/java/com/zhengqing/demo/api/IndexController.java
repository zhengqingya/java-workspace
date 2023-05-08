package com.zhengqing.demo.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 首页
 * </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2019/8/19 13:49
 */
@RestController
public class IndexController {

    @Value(value = "${spring.application.name}")
    private String applicationName;

    @GetMapping("/index")
    public String index() {
        return "您好，欢迎访问【" + applicationName + "】";
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(required = false) String msg) throws InterruptedException {
        // 模拟业务耗时处理流程
        Thread.sleep(2 * 1000L);
        return "hello: " + msg;
    }

}
