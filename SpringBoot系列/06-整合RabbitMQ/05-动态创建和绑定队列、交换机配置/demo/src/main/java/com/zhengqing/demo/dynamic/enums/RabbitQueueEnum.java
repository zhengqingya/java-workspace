package com.zhengqing.demo.dynamic.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p> RabbitMQ 队列枚举 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/6/6 10:42
 */
@Getter
@AllArgsConstructor
public enum RabbitQueueEnum {

    /**
     * 默认队列
     */
    DEFAULT_QUEUE("default_queue");

    /**
     * 队列名称
     */
    private String name;

}
