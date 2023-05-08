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

    // ================== ↓↓↓↓↓↓ 死信队列 ↓↓↓↓↓↓ ==================

    /**
     * 单条消息-设置TTL过期时间 - 3秒
     */
    String TTL_MSG_EXCHANGE = "ttl.msg.exchange";
    String TTL_MSG_QUEUE = "ttl_msg_queue";
    String TTL_MSG_ROUTING_KEY = "ttl_msg_routing_key";
    String TTL_MSG_TIME = String.valueOf(1000 * 3);

    /**
     * 队列消息-设置TTL过期时间 - 3秒 (该队列的所有消息都有统一的过期时间)
     * 如果ttl设置为0，表示消息不能立马消费则会被立即丢掉
     */
    String TTL_QUEUE_EXCHANGE = "ttl.exchange";
    String TTL_QUEUE_QUEUE = "ttl_queue_queue";
    String TTL_QUEUE_ROUTING_KEY = "ttl_queue_routing_key";
    long TTL_QUEUE_TIME = 1000 * 3;


    /**
     * 死信队列
     */
    String DLX_EXCHANGE = "dlx_exchange";
    String DLX_QUEUE = "dlx_queue";
    String DLX_ROUTING_KEY = "dlx_routing_key";

    /**
     * 普通订单队列 - 过期时间-30分钟
     */
    String ORDER_EXCHANGE = "order_exchange";
    String ORDER_QUEUE = "order_queue";
    String ORDER_ROUTING_KEY = "order_routing_key";
    int ORDER_QUEUE_TTL_TIME = 1000 * 60 * 30;

    /**
     * 延时队列 - mq插件方式'rabbitmq-delayed-message-exchange'
     */
    String PLUGIN_DELAY_EXCHANGE = "plugin_delay_exchange";
    String PLUGIN_DELAY_ROUTING_KEY = "plugin_delay_routing_key";
    String PLUGIN_DELAY_QUEUE = "plugin_delay_queue";

}
