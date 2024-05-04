package com.zhengqing.test.api;

import com.zhengqing.mytest.util.TestUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class TestController {

    @Resource
    private TestUtil testUtil;

    @GetMapping("/hello") // http://127.0.0.1:83/api/hello
    public String hello() {
        this.testUtil.hello();
        return "hi";
    }

}
