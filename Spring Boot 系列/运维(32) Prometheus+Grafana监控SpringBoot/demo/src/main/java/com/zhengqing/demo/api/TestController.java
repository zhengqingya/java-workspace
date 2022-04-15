package com.zhengqing.demo.api;


import cn.hutool.core.date.DateTime;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/5 2:36 下午
 */
@Slf4j
@RestController
@RequestMapping("/test")
@Api(tags = "测试api")
public class TestController {

    @GetMapping("time")
    @ApiOperation("time")
    public String time() {
        log.info("time: {}", DateTime.now());
        return DateTime.now().toString();
    }

    public static final Map<String, Object> map = new ConcurrentHashMap<>();


    /**
     * http://127.0.0.1:9200/test/test
     */
    @GetMapping("test")
    @ApiOperation("test")
    @SneakyThrows(Exception.class)
    public String test() {
        for (int i = 0; i < 10000000; i++) {
            log.info("time: {}", DateTime.now());
//            TimeUnit.SECONDS.sleep(3);
            map.put(i + "", new Object());
        }
        return "ok";
    }

}
