package com.zhengqing.demo.controller;

import com.zhengqing.demo.rabbitmq.direct.DirectMsgProducer;
import com.zhengqing.demo.rabbitmq.fanout.FanoutMsgProducer;
import com.zhengqing.demo.rabbitmq.rpc.RpcMsgProducer;
import com.zhengqing.demo.rabbitmq.simple.SimpleMsgProducer;
import com.zhengqing.demo.rabbitmq.topic.TopicMsgProducer;
import com.zhengqing.demo.rabbitmq.work.WorkMsgProducer;
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

    @Autowired
    private WorkMsgProducer workMsgProducer;

    @Autowired
    private FanoutMsgProducer fanoutMsgProducer;

    @Autowired
    private DirectMsgProducer directMsgProducer;

    @Autowired
    private TopicMsgProducer topicMsgProducer;

    @Autowired
    private RpcMsgProducer rpcMsgProducer;

    @ApiOperation("simpleQueue")
    @PostMapping("simpleQueue")
    public String simpleQueue() {
        this.simpleMsgProducer.send();
        return "SUCCESS";
    }

    @ApiOperation("workQueue")
    @PostMapping("workQueue")
    public String workQueue() {
        this.workMsgProducer.send();
        return "SUCCESS";
    }

    @ApiOperation("fanoutExchange")
    @PostMapping("fanoutExchange")
    public String fanoutExchange() {
        this.fanoutMsgProducer.send();
        return "SUCCESS";
    }

    @ApiOperation("directExchange1")
    @PostMapping("directExchange1")
    public String directExchange1() {
        this.directMsgProducer.send1();
        return "SUCCESS";
    }

    @ApiOperation("directExchange2")
    @PostMapping("directExchange2")
    public String directExchange2() {
        this.directMsgProducer.send2();
        return "SUCCESS";
    }

    @ApiOperation("topicExchange1")
    @PostMapping("topicExchange1")
    public String topicExchange1() {
        this.topicMsgProducer.send1();
        return "SUCCESS";
    }

    @ApiOperation("topicExchange2")
    @PostMapping("topicExchange2")
    public String topicExchange2() {
        this.topicMsgProducer.send2();
        return "SUCCESS";
    }

    @ApiOperation("rpcQueue")
    @PostMapping("rpcQueue")
    public String rpcQueue() {
        this.rpcMsgProducer.send();
        return "SUCCESS";
    }

}
