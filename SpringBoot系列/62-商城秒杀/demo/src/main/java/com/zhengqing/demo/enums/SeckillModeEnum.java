package com.zhengqing.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * <p> 秒杀模式 </p>
 *
 * @author zhengqingya
 * @description ✘ or ✔
 * @date 2021/1/13 20:25
 */
@Getter
@AllArgsConstructor
public enum SeckillModeEnum {

    秒杀一_不加锁("✘"),
    秒杀二_加锁_ReentrantLock("✘"),
    秒杀三_加锁_ReentrantLock_AOP("✔"),
    秒杀四_加锁_DB悲观锁("✔"),
    秒杀五_加锁_DB乐观锁("✔"),
    秒杀六_加锁_Redis分布式锁("✘"),
    秒杀七_加锁_Redis分布式锁_AOP("✔");

    private final String result;

    public static final String MODE_VALUES = String.join(",", Arrays.stream(SeckillModeEnum.values()).map(SeckillModeEnum::name).collect(Collectors.toList()));

    static {

    }

}
