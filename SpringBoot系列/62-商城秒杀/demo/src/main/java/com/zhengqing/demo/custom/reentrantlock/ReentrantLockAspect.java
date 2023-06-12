package com.zhengqing.demo.custom.reentrantlock;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p> aop切面 - ReentrantLock同步锁 </p>
 *
 * @author zhengqingya
 * @description 锁上移：在调用事务之前上锁，即锁包含事务
 * order越小越是最先执行，但更重要的是最先执行的最后结束 @Transactional默认的order是Integer.MAX_VALUE。  {@link org.springframework.transaction.annotation.EnableTransactionManagement}
 * eg: 如果在启动类上定义 @EnableTransactionManagement(order = -1) 即执行优先级比这里的高 =》事务包含锁，秒杀场景又会出现超卖
 * @date 2021/10/8 9:36
 */
@Slf4j
@Aspect
@Order(1)
@Component
public class ReentrantLockAspect {

    private final Lock lock = new ReentrantLock(true);

    @SneakyThrows
    @Around("@annotation(com.zhengqing.demo.custom.reentrantlock.ReentrantLock)")
    public Object doAround(ProceedingJoinPoint pjp) {
        try {
            // 加锁
            this.lock.lock();
            return pjp.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            // 释放锁
            this.lock.unlock();
        }
    }

}
