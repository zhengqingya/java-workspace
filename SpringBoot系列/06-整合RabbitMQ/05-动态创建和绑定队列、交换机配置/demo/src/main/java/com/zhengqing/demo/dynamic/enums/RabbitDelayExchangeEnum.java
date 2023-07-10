package com.zhengqing.demo.dynamic.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p> RabbitMQ 延迟交换机枚举 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/6/6 10:42
 */
@Getter
@AllArgsConstructor
public enum RabbitDelayExchangeEnum {

    /**
     * 默认交换机
     */
    DEFAULT_DELAY_EXCHANGE("default_delay_exchange");

    /**
     * 交换机名称
     */
    private String name;

}

