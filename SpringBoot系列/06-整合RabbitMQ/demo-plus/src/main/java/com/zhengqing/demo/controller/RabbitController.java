package com.zhengqing.demo.controller;

import com.zhengqing.demo.rabbitmq.direct.DirectMsgProducer;
import com.zhengqing.demo.rabbitmq.fanout.FanoutMsgProducer;
import com.zhengqing.demo.rabbitmq.rpc.RpcMsgProducer;
import com.zhengqing.demo.rabbitmq.simple.SimpleMsgProducer;
import com.zhengqing.demo.rabbitmq.topic.TopicMsgProducer;
import com.zhengqing.demo.rabbitmq.work.WorkMsgProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试mq-7种工作模式")
@RestController
@RequestMapping("/api/mq")
@RequiredArgsConstructor
public class RabbitController {

    private final SimpleMsgProducer simpleMsgProducer;
    private final WorkMsgProducer workMsgProducer;
    private final FanoutMsgProducer fanoutMsgProducer;
    private final DirectMsgProducer directMsgProducer;
    private final TopicMsgProducer topicMsgProducer;
    private final RpcMsgProducer rpcMsgProducer;

    @ApiOperation("简单模式")
    @PostMapping("simpleQueue")
    public String simpleQueue() {
        this.simpleMsgProducer.send();
        return "SUCCESS";
    }

    @ApiOperation("工作队列模式")
    @PostMapping("workQueue")
    public String workQueue() {
        this.workMsgProducer.send();
        return "SUCCESS";
    }

    @ApiOperation("发布订阅模式")
    @PostMapping("fanoutExchange")
    public String fanoutExchange() {
        this.fanoutMsgProducer.send();
        return "SUCCESS";
    }

    @ApiOperation("路由模式1")
    @PostMapping("directExchange1")
    public String directExchange1() {
        this.directMsgProducer.send1();
        return "SUCCESS";
    }

    @ApiOperation("路由模式2")
    @PostMapping("directExchange2")
    public String directExchange2() {
        this.directMsgProducer.send2();
        return "SUCCESS";
    }

    @ApiOperation("主题模式1")
    @PostMapping("topicExchange1")
    public String topicExchange1() {
        this.topicMsgProducer.send1();
        return "SUCCESS";
    }

    @ApiOperation("主题模式2")
    @PostMapping("topicExchange2")
    public String topicExchange2() {
        this.topicMsgProducer.send2();
        return "SUCCESS";
    }

    @ApiOperation("RPC模式")
    @PostMapping("rpcQueue")
    public String rpcQueue() {
        this.rpcMsgProducer.send();
        return "SUCCESS";
    }

}
