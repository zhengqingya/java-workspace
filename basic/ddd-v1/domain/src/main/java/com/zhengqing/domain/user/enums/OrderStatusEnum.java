package com.zhengqing.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    UNPAID("UNPAID", "未支付"),
    PAID("PAID", "已支付");

    private final String status;
    private final String desc;

}
