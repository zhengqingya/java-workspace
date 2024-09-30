package com.zhengqing.common.netty.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p> 登录 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/26 17:59
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NettyLogin {

    private String accessToken;

}
