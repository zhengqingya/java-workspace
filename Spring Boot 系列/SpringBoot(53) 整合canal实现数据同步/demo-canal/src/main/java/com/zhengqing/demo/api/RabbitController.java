package com.zhengqing.demo.api;

import com.zhengqing.demo.rabbitmq.SimpleMsgProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试mq")
@RestController
@RequestMapping("/mq")
public class RabbitController {

    @Autowired
    private SimpleMsgProducer simpleMsgProducer;

    @ApiOperation("simpleQueue")
    @PostMapping("simpleQueue")
    public String simpleQueue() {
        this.simpleMsgProducer.send();
        return "SUCCESS";
    }


}
