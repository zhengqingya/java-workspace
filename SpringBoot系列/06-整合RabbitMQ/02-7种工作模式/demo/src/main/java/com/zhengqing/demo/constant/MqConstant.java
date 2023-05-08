package com.zhengqing.demo.constant;

/**
 * <p> 全局常用变量 - mq </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/8/9 11:12
 */
public interface MqConstant {

    /**
     * 简单模式
     */
    String SIMPLE_QUEUE = "simple_queue";
    /**
     * 工作队列模式
     */
    String WORK_QUEUE = "work_queue";
    /**
     * 发布订阅模式
     */
    String FANOUT_EXCHANGE = "fanout.exchange";
    String FANOUT_QUEUE_1 = "fanout_queue_1";
    String FANOUT_QUEUE_2 = "fanout_queue_2";
    /**
     * 路由模式
     */
    String DIRECT_EXCHANGE = "direct.exchange";
    String DIRECT_QUEUE_1 = "direct_queue_1";
    String DIRECT_QUEUE_2 = "direct_queue_2";
    /**
     * 通配符模式
     */
    String TOPIC_EXCHANGE = "topic.exchange";
    String TOPIC_QUEUE_1 = "topic_queue_1";
    String TOPIC_QUEUE_2 = "topic_queue_2";
    /**
     * RPC模式
     */
    String RPC_QUEUE = "rpc_queue";

}
