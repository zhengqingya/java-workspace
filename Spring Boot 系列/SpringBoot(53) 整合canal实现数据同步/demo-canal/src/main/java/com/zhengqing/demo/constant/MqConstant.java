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
     * 路由模式
     */
    String CANAL_EXCHANGE = "canal.exchange";
    String CANAL_QUEUE = "canal_queue";
    String CANAL_ROUTING_KEY = "canal_routing_key";

}
