package com.zhengqing.common.netty.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p> jwt用户信息 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/27 14:24
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NettyJwtUser {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 终端类型
     */
    private Integer terminal;
}
