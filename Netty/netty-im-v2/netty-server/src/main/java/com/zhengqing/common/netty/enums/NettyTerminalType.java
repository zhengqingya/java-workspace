package com.zhengqing.common.netty.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p> 用户平台 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/27 17:10
 */
@Getter
@AllArgsConstructor
public enum NettyTerminalType {

    /**
     * web
     */
    WEB(0, "web"),
    /**
     * app
     */
    APP(1, "app"),
    /**
     * pc
     */
    PC(2, "pc");

    /**
     * 平台
     */
    private final Integer type;
    /**
     * 描述
     */
    private final String desc;

    private static final List<NettyTerminalType> LIST = Arrays.asList(NettyTerminalType.values());

    @SneakyThrows
    public static NettyTerminalType getType(Integer type) {
        for (NettyTerminalType typeEnum : LIST) {
            if (typeEnum.type.equals(type)) {
                return typeEnum;
            }
        }
        throw new Exception("netty用户平台未知！");
    }

    public static List<Integer> typeList() {
        return Arrays.stream(values()).map(NettyTerminalType::getType).collect(Collectors.toList());
    }

}
