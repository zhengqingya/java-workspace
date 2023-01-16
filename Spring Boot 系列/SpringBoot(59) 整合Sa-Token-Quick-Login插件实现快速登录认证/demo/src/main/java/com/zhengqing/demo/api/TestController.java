package com.zhengqing.demo.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    /**
     * 浏览器访问测试： http://localhost:666
     */
    @RequestMapping({"/", "/index"})
    public String index() {
        String str = "<br />"
                + "<h1 style='text-align: center;'>资源页 （登录后才可进入本页面） </h1>"
                + "<hr/>"
                + "<p style='text-align: center;'> zhengqingya tips </p>";
        return str;
    }
}
