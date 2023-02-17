package com.zhengqing.demo.config;

import com.plumelog.trace.aspect.AbstractAspect;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * <p> plumelog 全局打点 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/2/17 16:40
 */
@Aspect
@Component
public class AspectConfig extends AbstractAspect {

    @SneakyThrows
    @Around("within(com.zhengqing..*))")
    public Object around(JoinPoint joinPoint) {
        return this.aroundExecute(joinPoint);
    }

}