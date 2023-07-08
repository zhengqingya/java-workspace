package com.zhengqing.demo.controller;

import com.zhengqing.demo.rabbitmq.delay.dlx.DlxProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试mq-死信队列")
@RestController
@RequestMapping("/api/mq")
@RequiredArgsConstructor
public class DlxController {

    private final DlxProducer dlxProducer;

    @ApiOperation("sendDlxMsg")
    @PostMapping("sendDlxMsg")
    public String sendDlxMsg() {
        this.dlxProducer.send();
        return "SUCCESS";
    }


}
