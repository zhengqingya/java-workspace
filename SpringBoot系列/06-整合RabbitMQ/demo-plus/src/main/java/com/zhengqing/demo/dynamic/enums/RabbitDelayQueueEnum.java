package com.zhengqing.demo.dynamic.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p> RabbitMQ 延迟队列枚举 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/6/6 10:42
 */
@Getter
@AllArgsConstructor
public enum RabbitDelayQueueEnum {

    /**
     * 默认延迟队列
     */
    DEFAULT_DELAY_QUEUE("default_delay_queue");

    /**
     * 队列名称
     */
    private String name;

}

