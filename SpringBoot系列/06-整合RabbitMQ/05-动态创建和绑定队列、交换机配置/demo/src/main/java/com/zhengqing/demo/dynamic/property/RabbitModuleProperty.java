package com.zhengqing.demo.dynamic.property;

import com.zhengqing.demo.dynamic.enums.RabbitExchangeTypeEnum;
import lombok.Data;

import java.util.Map;


/**
 * <p> RabbitMQ 队列和交换机机绑定关系实体对象 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/7/8 10:37
 */
@Data
public class RabbitModuleProperty {

    /**
     * 生产者
     */
    private String producer;
    /**
     * 消费者
     */
    private String consumer;
    /**
     * 消息重试
     */
    private String retry;
    /**
     * 自动确认
     */
    private Boolean autoAck = true;

    /**
     * 交换机信息
     */
    private Exchange exchange;
    /**
     * 路由Key
     */
    private String routingKey;
    /**
     * 队列信息
     */
    private Queue queue;

    /**
     * 交换机信息类
     */
    @Data
    public static class Exchange {
        /**
         * 交换机类型
         * 默认直连交换机
         */
        private RabbitExchangeTypeEnum type = RabbitExchangeTypeEnum.DIRECT;

        /**
         * 交换机名称
         */
        private String name;

        /**
         * 是否持久化
         * 默认true持久化，重启消息不会丢失
         */
        private boolean durable = true;

        /**
         * 当所有队绑定列均不在使用时，是否自动删除交换机
         * 默认false，不自动删除
         */
        private boolean autoDelete = false;

        /**
         * 交换机其他参数
         */
        private Map<String, Object> arguments;
    }

    /**
     * 队列信息类
     */
    @Data
    public static class Queue {
        /**
         * 队列名称
         */
        private String name;

        /**
         * 是否持久化
         * 默认true持久化，重启消息不会丢失
         */
        private boolean durable = true;

        /**
         * 是否具有排他性
         * 默认false，可多个消费者消费同一个队列
         */
        private boolean exclusive = false;

        /**
         * 当消费者均断开连接，是否自动删除队列
         * 默认false,不自动删除，避免消费者断开队列丢弃消息
         */
        private boolean autoDelete = false;

        /**
         * 绑定死信队列的交换机名称
         */
        private String deadLetterExchange;

        /**
         * 绑定死信队列的路由key
         */
        private String deadLetterRoutingKey;

        /**
         * 其他参数
         */
        private Map<String, Object> arguments;
    }

}
