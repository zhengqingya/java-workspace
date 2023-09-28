package com.zhengqing.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 响应码枚举 - 可参考HTTP状态码的语义
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/8/22 11:09
 */
@Getter
@AllArgsConstructor
public enum ApiResultCodeEnum {

    // 成功
    SUCCESS(200, "SUCCESS"),
    // 失败
    FAIL(400, "FAIL");

    private final int code;
    private final String desc;

}
