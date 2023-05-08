package com.zhengqing.demo.controller;

import com.zhengqing.demo.rabbitmq.delay.plugins.DelayProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试mq-延时队列")
@RestController
@RequestMapping("/mq")
public class DelayController {

    @Autowired
    private DelayProducer delayProducer;

    @ApiOperation("sendDelayMsg")
    @PostMapping("sendDelayMsg")
    public String sendDelayMsg() {
        this.delayProducer.send();
        return "SUCCESS";
    }

}
