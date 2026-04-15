package com.zhengqing.demo.api;


import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/5 2:36 下午
 */
@Slf4j
@RestController
@RequestMapping("")
@Api(tags = "测试api")
public class TestController {

    @GetMapping("time") // http://127.0.0.1/time
    @ApiOperation("time")
    public String time() {
        log.info("time: {}", DateUtil.date());
        return DateUtil.date().toString();
    }

    @GetMapping("log/test") // http://127.0.0.1/log/test?keyword=666
    @ApiOperation("skywalking日志测试")
    public Map<String, Object> logTest(@RequestParam(defaultValue = "skywalking-log-test") String keyword) {
        log.debug("SkyWalking log test start, keyword={}", keyword);
        log.info("SkyWalking info log, keyword={}, now={}", keyword, DateUtil.now());
        log.warn("SkyWalking warn log, keyword={}", keyword);
        try {
            int value = 1 / 0;
            log.info("unexpected value={}", value);
        } catch (Exception e) {
            log.error("SkyWalking error log, keyword={}", keyword, e);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("keyword", keyword);
        result.put("time", DateUtil.now());
        result.put("message", "SkyWalking log test finished");
        return result;
    }

}
