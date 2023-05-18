package com.zhengqing.demo.api;


import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/5 2:36 下午
 */
@Slf4j
@RestController
@RequestMapping("/api")
@Api(tags = "测试api")
public class TestController {
    @ApiOperation("time")
    @GetMapping("time") // http://127.0.0.1/api/time
    public Object time() {
        log.info("time: {}", DateUtil.date());
        return new HashMap<String, Object>() {{
            this.put("code", 200);
            this.put("msg", "ok");
            this.put("data", "ok");
        }};
    }

}
