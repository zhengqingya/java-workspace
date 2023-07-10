package com.zhengqing.demo.dynamic.service.impl;

import cn.hutool.json.JSONUtil;
import com.google.common.reflect.TypeToken;
import com.rabbitmq.client.Channel;
import com.zhengqing.demo.dynamic.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

import java.io.IOException;

/**
 * <p> 消费者实现类封装 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/7/10 15:59
 */
@Slf4j
public abstract class AbsConsumerService<T> implements ConsumerService {

    private Class<T> clazz = (Class<T>) new TypeToken<T>(this.getClass()) {
    }.getRawType();

    /**
     * 消息
     */
    private Message message;
    /**
     * 通道
     */
    private Channel channel;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        this.message = message;
        this.channel = channel;
        String body = new String(message.getBody());
        T data = null;
        try {
            data = JSONUtil.toBean(body, this.clazz);
        } catch (Exception e) {
            data = (T) body;
        }
        this.onConsumer(data);
    }

    /**
     * 扩展消费方法，对消息进行封装
     *
     * @param data 消息数据
     */
    public void onConsumer(T data) throws Exception {
        log.error("未对此方法进行实现: {}", data);
    }

    /**
     * *************************** ack正常确认 ***************************
     */

    /**
     * 确认消息 -- 只确认当前的 deliveryTag 对应的单条消息
     */
    protected void ack() throws IOException {
        this.ack(Boolean.FALSE);
    }

    /**
     * 是否自动确认
     *
     * @param multiple 指定是否开启批量确认模式。false：只确认当前的 deliveryTag 对应的单条消息；true：同时确认 deliveryTag 值小于等于当前值的所有未确认消息。
     */
    protected void ack(Boolean multiple) throws IOException {
        this.channel.basicAck(this.message.getMessageProperties().getDeliveryTag(), multiple);
    }

    /**
     * *************************** 错误确认 ***************************
     *  basicReject: 只能否定一条消息
     *  basicNack: 可以否定一条或者多条消息
     */

    /**
     * 拒绝消息 -- 直接丢弃消息
     */
    protected void basicReject() throws IOException {
        this.basicReject(Boolean.FALSE);
    }

    /**
     * 拒绝消息
     *
     * @param requeue true：重新入队再次消费 -- 会导致死循环， false：丢弃消息
     */
    protected void basicReject(Boolean requeue) throws IOException {
        this.channel.basicReject(this.message.getMessageProperties().getDeliveryTag(), requeue);
    }


    /**
     * 拒绝消息 -- 单条消息拒绝
     */
    protected void basicNack() throws IOException {
        this.basicNack(Boolean.FALSE, Boolean.FALSE);
    }

    /**
     * 拒绝消息
     *
     * @param multiple 是否批量确认（如果设置为true，则表示确认所有小于等于deliveryTag的消息）
     * @param requeue  是否重新将消息放回队列:
     *                 true -- 允许消息重新排队 （业务中注意一直出现死循环处理，导致消息积压）
     *                 false -- 拒绝重新排队消息，丢弃消息
     */
    protected void basicNack(Boolean multiple, Boolean requeue) throws IOException {
        this.channel.basicNack(this.message.getMessageProperties().getDeliveryTag(), multiple, requeue);
    }

}