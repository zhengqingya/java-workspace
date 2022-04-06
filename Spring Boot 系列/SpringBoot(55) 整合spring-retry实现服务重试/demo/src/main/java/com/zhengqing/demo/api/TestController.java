package com.zhengqing.demo.api;


import cn.hutool.core.date.DateTime;
import com.zhengqing.demo.service.IBusinessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    @Resource
    private IBusinessService businessService;

    @ApiOperation("服务重试")
    @GetMapping("retryable")
    @SneakyThrows(Exception.class)
    public String retryable(@RequestParam(required = false) String msg) {
        return this.businessService.retry(msg);
    }

    @GetMapping("time")
    @ApiOperation("time")
    public String time() {
        log.info("time: {}", DateTime.now());
        return DateTime.now().toString();
    }

}
