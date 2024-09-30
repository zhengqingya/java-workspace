package com.zhengqing.common.netty.model;

import com.zhengqing.common.netty.enums.NettyTerminalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p> 消息关联人 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/27 17:07
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NettyChatUserBO {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户终端类型
     * {@link NettyTerminalType}
     */
    private Integer terminal;

}
