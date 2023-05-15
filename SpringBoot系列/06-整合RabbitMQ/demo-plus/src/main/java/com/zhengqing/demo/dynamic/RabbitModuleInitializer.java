package com.zhengqing.demo.dynamic;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.zhengqing.demo.config.RabbitModuleProperty;
import com.zhengqing.demo.enums.RabbitExchangeTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> RabbitMQ队列初始化器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/7/8 10:37
 */
@Slf4j
public class RabbitModuleInitializer implements SmartInitializingSingleton {

    private AmqpAdmin amqpAdmin;

    private RabbitModuleProperty rabbitModuleProperty;

    public RabbitModuleInitializer(AmqpAdmin amqpAdmin, RabbitModuleProperty rabbitModuleProperty) {
        this.amqpAdmin = amqpAdmin;
        this.rabbitModuleProperty = rabbitModuleProperty;
    }

    @Override
    public void afterSingletonsInstantiated() {
        log.info("RabbitMQ 根据配置动态创建和绑定队列、交换机");
        this.declareRabbitModule();
    }

    /**
     * RabbitMQ 根据配置动态创建和绑定队列、交换机
     */
    private void declareRabbitModule() {
        List<RabbitModule> rabbitModuleList = this.rabbitModuleProperty.getModuleList();
        if (CollectionUtil.isEmpty(rabbitModuleList)) {
            return;
        }
        for (RabbitModule rabbitModule : rabbitModuleList) {
            this.configParamValidate(rabbitModule);

            // 队列
            Queue queue = this.convertQueue(rabbitModule.getQueue());
            // 交换机
            Exchange exchange = this.convertExchange(rabbitModule.getExchange());
            // 绑定关系
            String routingKey = rabbitModule.getRoutingKey();
            String queueName = rabbitModule.getQueue().getName();
            String exchangeName = rabbitModule.getExchange().getName();
            Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, routingKey, null);

            // 创建队列
            this.amqpAdmin.declareQueue(queue);
            this.amqpAdmin.declareExchange(exchange);
            this.amqpAdmin.declareBinding(binding);
        }
    }

    /**
     * RabbitMQ动态配置参数校验
     */
    public void configParamValidate(RabbitModule rabbitModule) {
        String routingKey = rabbitModule.getRoutingKey();

        Assert.isTrue(StrUtil.isNotBlank(routingKey), "RoutingKey 未配置");

        Assert.isTrue(rabbitModule.getExchange() != null, "routingKey:{}未配置exchange", routingKey);
        Assert.isTrue(StrUtil.isNotBlank(rabbitModule.getExchange().getName()), "routingKey:{}未配置exchange的name属性", routingKey);

        Assert.isTrue(rabbitModule.getQueue() != null, "routingKey:{}未配置queue", routingKey);
        Assert.isTrue(StrUtil.isNotBlank(rabbitModule.getQueue().getName()), "routingKey:{}未配置queue的name属性", routingKey);
    }

    /**
     * 转换生成RabbitMQ队列
     */
    public Queue convertQueue(RabbitModule.Queue queue) {
        Map<String, Object> arguments = queue.getArguments();

        // 转换ttl的类型为long
        if (arguments != null && arguments.containsKey("x-message-ttl")) {
            arguments.put("x-message-ttl", Convert.toLong(arguments.get("x-message-ttl")));
        }

        // 是否需要绑定死信队列
        String deadLetterExchange = queue.getDeadLetterExchange();
        String deadLetterRoutingKey = queue.getDeadLetterRoutingKey();
        if (StrUtil.isNotBlank(deadLetterExchange) && StrUtil.isNotBlank(deadLetterRoutingKey)) {
            if (arguments == null) {
                arguments = new HashMap<>(2);
            }
            // 设置死信交换机
            arguments.put("x-dead-letter-exchange", deadLetterExchange);
            // 设置死信
            arguments.put("x-dead-letter-routing-key", deadLetterRoutingKey);
        }

        return new Queue(queue.getName(), queue.isDurable(), queue.isExclusive(), queue.isAutoDelete(), arguments);
    }


    /**
     * 转换生成RabbitMQ交换机
     */
    public Exchange convertExchange(RabbitModule.Exchange exchangeInfo) {
        AbstractExchange exchange = null;
        RabbitExchangeTypeEnum exchangeType = exchangeInfo.getType();
        String exchangeName = exchangeInfo.getName();
        boolean isDurable = exchangeInfo.isDurable();
        boolean isAutoDelete = exchangeInfo.isAutoDelete();
        Map<String, Object> arguments = exchangeInfo.getArguments();
        switch (exchangeType) {
            case DIRECT:
                exchange = new DirectExchange(exchangeName, isDurable, isAutoDelete, arguments);
                break;
            case TOPIC:
                exchange = new TopicExchange(exchangeName, isDurable, isAutoDelete, arguments);
                break;
            case FANOUT:
                exchange = new FanoutExchange(exchangeName, isDurable, isAutoDelete, arguments);
                break;
            case HEADERS:
                exchange = new HeadersExchange(exchangeName, isDurable, isAutoDelete, arguments);
                break;
            case DELAY:
                if (arguments == null) {
                    arguments = new HashMap<String, Object>(1) {{
                        this.put("x-delayed-type", "direct");
                    }};
                }
                exchange = new CustomExchange(exchangeName, "x-delayed-message", isDurable, isAutoDelete, arguments);
                break;
        }
        return exchange;
    }

}
