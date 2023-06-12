package com.zhengqing.demo.custom.redislock;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * <p> aop切面 - Redis分布式锁 </p>
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
@RequiredArgsConstructor
public class RedisLockAspect {

    private final RedissonClient redissonClient;

    @SneakyThrows
    @Around("@annotation(redisLock)")
    public Object doAround(ProceedingJoinPoint pjp, RedisLock redisLock) {
        RLock lock = this.redissonClient.getLock(redisLock.key());
        try {
            // 加锁
            lock.lock();
            return pjp.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

}
