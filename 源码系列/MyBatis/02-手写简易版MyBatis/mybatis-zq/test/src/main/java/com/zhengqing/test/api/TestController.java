package com.zhengqing.test.api;

import com.zhengqing.test.mapper.DemoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 测试接口 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/5/5 22:33
 */
@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private DemoMapper demoMapper;

    @GetMapping("/test")
    public Object test() {
        return this.demoMapper.findOne(1);
    }


}
