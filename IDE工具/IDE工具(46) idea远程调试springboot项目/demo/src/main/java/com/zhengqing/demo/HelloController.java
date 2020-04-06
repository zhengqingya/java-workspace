package com.zhengqing.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p> 测试Controller </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/3/30 21:34
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("当前时间：" + now);
        return String.valueOf(now);
    }

}
