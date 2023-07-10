package com.zhengqing.demo.dynamic.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p> RabbitMQ 交换机枚举 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/6/6 10:42
 */
@Getter
@AllArgsConstructor
public enum RabbitExchangeEnum {

    /**
     * 默认交换机
     */
    DEFAULT_EXCHANGE("default_exchange");

    /**
     * 交换机名称
     */
    private String name;

}

