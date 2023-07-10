package com.zhengqing.demo.dynamic;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.zhengqing.demo.dynamic.enums.RabbitExchangeTypeEnum;
import com.zhengqing.demo.dynamic.property.RabbitModuleProperty;
import com.zhengqing.demo.dynamic.property.RabbitModulePropertys;
import com.zhengqing.demo.dynamic.retry.CustomRetryListener;
import com.zhengqing.demo.dynamic.service.impl.AbsProducerService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p> RabbitMQ动态队列初始化器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/7/8 10:37
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitMqDynamicConfig implements SmartInitializingSingleton {

    /**
     * MQ链接工厂
     */
    private final ConnectionFactory connectionFactory;
    /**
     * MQ操作管理器
     */
    private final AmqpAdmin amqpAdmin;
    /**
     * YML配置参数
     */
    private final RabbitModulePropertys rabbitModulePropertys;
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.listener.simple.retry.max-attempts}")
    private Integer maxAttempts;

//    public RabbitMqDynamicInitializer(ConnectionFactory connectionFactory, AmqpAdmin amqpAdmin, RabbitModulePropertys rabbitModulePropertys) {
//        this.connectionFactory = connectionFactory;
//        this.amqpAdmin = amqpAdmin;
//        this.rabbitModulePropertys = rabbitModulePropertys;
//    }

    @Override
    public void afterSingletonsInstantiated() {
        log.info("RabbitMQ 根据配置动态创建和绑定队列、交换机");
        this.declareRabbitModule();
    }

    /**
     * RabbitMQ 根据配置动态创建和绑定队列、交换机
     */
    private void declareRabbitModule() {
        List<RabbitModuleProperty> rabbitModulePropertyList = this.rabbitModulePropertys.getModuleList();
        if (CollectionUtil.isEmpty(rabbitModulePropertyList)) {
            return;
        }
        for (RabbitModuleProperty rabbitModuleProperty : rabbitModulePropertyList) {
            // 配置参数校验
            this.configParamValidate(rabbitModuleProperty);

            // 队列
            Queue queue = this.convertQueue(rabbitModuleProperty.getQueue());
            // 交换机
            Exchange exchange = this.convertExchange(rabbitModuleProperty.getExchange());
            // 绑定关系
            this.queueBindExchange(queue, exchange, rabbitModuleProperty);

            // 绑定生产者
            this.bindProducer(rabbitModuleProperty);
            // 绑定消费者
            this.bindConsumer(queue, exchange, rabbitModuleProperty);
        }
    }

    /**
     * RabbitMQ动态配置参数校验
     */
    public void configParamValidate(RabbitModuleProperty rabbitModuleProperty) {
        String routingKey = rabbitModuleProperty.getRoutingKey();

        Assert.isTrue(StrUtil.isNotBlank(routingKey), "[RabbitMQ] RoutingKey 未配置");

        Assert.isTrue(rabbitModuleProperty.getExchange() != null, "[RabbitMQ] routingKey:{} 未配置exchange", routingKey);
        Assert.isTrue(StrUtil.isNotBlank(rabbitModuleProperty.getExchange().getName()), "[RabbitMQ] routingKey:{} 未配置exchange的name属性", routingKey);

        Assert.isTrue(rabbitModuleProperty.getQueue() != null, "[RabbitMQ] routingKey:{} 未配置queue", routingKey);
        Assert.isTrue(StrUtil.isNotBlank(rabbitModuleProperty.getQueue().getName()), "[RabbitMQ] routingKey:{} 未配置queue的name属性", routingKey);

        if (StrUtil.isNotBlank(rabbitModuleProperty.getRetry())) {
            Assert.isTrue(StrUtil.isNotBlank(rabbitModuleProperty.getConsumer()), "[RabbitMQ] queue:{} 配置消息重试但未配置消费者", rabbitModuleProperty.getQueue().getName());
        }
    }

    /**
     * 转换生成RabbitMQ队列
     */
    public Queue convertQueue(RabbitModuleProperty.Queue queue) {
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
                arguments = new HashMap<>(16);
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
    public Exchange convertExchange(RabbitModuleProperty.Exchange exchangeInfo) {
        RabbitExchangeTypeEnum exchangeType = exchangeInfo.getType();
        String exchangeName = exchangeInfo.getName();
        boolean isDurable = exchangeInfo.isDurable();
        boolean isAutoDelete = exchangeInfo.isAutoDelete();
        Map<String, Object> arguments = exchangeInfo.getArguments();

        // 根据类型生成交换机
        AbstractExchange exchange = null;
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
            default:
                log.warn("[RabbitMQ] 未匹配到交换机类型");
                break;
        }
        return exchange;
    }

    /**
     * 队列绑定交换机
     */
    private void queueBindExchange(Queue queue, Exchange exchange, RabbitModuleProperty rabbitModuleProperty) {
        log.debug("[RabbitMQ] 初始化交换机: {}", rabbitModuleProperty.getExchange().getName());
        String routingKey = rabbitModuleProperty.getRoutingKey();
        String queueName = rabbitModuleProperty.getQueue().getName();
        String exchangeName = rabbitModuleProperty.getExchange().getName();

        Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, routingKey, null);

        // 创建队列、交换机、建立绑定
        this.amqpAdmin.declareQueue(queue);
        this.amqpAdmin.declareExchange(exchange);
        this.amqpAdmin.declareBinding(binding);
        log.debug("[RabbitMQ] 队列绑定交换机 队列:{} 交换机:{} 交换机类型：{}", queueName, exchangeName, rabbitModuleProperty.getExchange().getType());
    }

    /**
     * 绑定生产者
     */
    private void bindProducer(RabbitModuleProperty rabbitModuleProperty) {
        try {
            String producer = rabbitModuleProperty.getProducer();
            if (StrUtil.isBlank(producer)) {
                return;
            }
            AbsProducerService producerService = SpringUtil.getBean(producer);
            producerService.setExchange(rabbitModuleProperty.getExchange().getName());
            producerService.setRoutingKey(rabbitModuleProperty.getRoutingKey());
            log.debug("[RabbitMQ] 绑定生产者: {}", producer);
        } catch (Exception e) {
            log.error("[RabbitMQ] 无法在容器中找到该生产者[{}]，若需要此生产者请做具体实现", rabbitModuleProperty.getConsumer());
            throw e;
        }
    }

    /**
     * 绑定消费者
     */
    @SneakyThrows
    private void bindConsumer(Queue queue, Exchange exchange, RabbitModuleProperty rabbitModuleProperty) {
        String retry = rabbitModuleProperty.getRetry();
        String consumer = rabbitModuleProperty.getConsumer();
        CustomRetryListener customRetryListener = null;
        try {
            if (StrUtil.isNotBlank(retry)) {
                customRetryListener = SpringUtil.getBean(retry);
            }
        } catch (Exception e) {
            log.error("[RabbitMQ] 无法在容器中找到该重试类[{}]，若需要重试请做具体实现", retry);
            throw e;
        }
        if (StrUtil.isBlank(retry)) {
            return;
        }
        try {
            ConsumerContainerFactory factory = ConsumerContainerFactory.builder()
                    .connectionFactory(this.connectionFactory)
                    .queue(queue)
                    .exchange(exchange)
                    .consumer(SpringUtil.getBean(consumer))
                    .retryListener(customRetryListener)
                    .autoAck(rabbitModuleProperty.getAutoAck())
                    .amqpAdmin(this.amqpAdmin)
                    .maxAttempts(this.maxAttempts)
                    .rabbitTemplate(this.rabbitTemplate)
                    .build();
            SimpleMessageListenerContainer container = factory.getObject();
            if (Objects.nonNull(container)) {
                container.start();
            }
            log.debug("[RabbitMQ] 绑定消费者: {}", consumer);
        } catch (Exception e) {
            log.error("[RabbitMQ] 无法在容器中找到该消费者[{}]，若需要此消费者请做具体实现", consumer);
            throw e;
        }
    }

}
