package com.zhengqing.demo.controller;

import cn.hutool.core.date.DateTime;
import com.zhengqing.demo.controller.retry.RetryProducerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "测试mq-消息重试")
@RestController
@RequestMapping("/api/mq/retry")
@RequiredArgsConstructor
public class RetryController {
    private final RetryProducerService retryProducerService;

    @ApiOperation("消息重试")
    @PostMapping("producer")
    public String producer() {
        String msgContent = "Hello World " + DateTime.now();
        this.retryProducerService.send(msgContent);
        return "SUCCESS";
    }

}
