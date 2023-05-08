package com.zhengqing.demo.api;


import cn.hutool.core.date.DateTime;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;

/**
 * <p> 测试重试 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/5 2:36 下午
 */
@Slf4j
@RestController
@RequestMapping("")
@Api(tags = "测试重试")
public class RetryController {

    /**
     * @Retryable 参数说明
     * value：抛出指定异常才会重试
     * maxAttempts：最大重试次数，默认3次
     * backoff：重试等待策略
     * recover：执行回调方法名称，必须在当前类中 -- tips:旧版本中无此参数，好像会自动对应失败回调方法
     */
    @ApiOperation("服务重试")
    @GetMapping("retryable")
    @SneakyThrows(Exception.class)
    @Retryable(value = RetryException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 1.5), recover = "recover")
    public String retry(@RequestParam String msg) {
        log.info("test被调用,时间：" + DateTime.now());
        final int a = 3;
        int num = new SecureRandom().nextInt();
        if (num % a == 0) {
            log.info("服务调用正常！");
            return "OK";
        }
        log.info("服务调用不正常。。。");
        throw new RetryException("服务调用不正常。。。");
    }

    @Recover
    public String recover(RetryException e, String msg) {
        log.info("执行回调方法: {}", msg);
        return "FAIL";
    }

}
