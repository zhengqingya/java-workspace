package com.zhengqing.user.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * api
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/4/1 16:02
 */
@RestController
// 支持跨域
@CrossOrigin(value = "*")
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/admin/hello")
    public String admin() {
        return "admin";
    }
}
