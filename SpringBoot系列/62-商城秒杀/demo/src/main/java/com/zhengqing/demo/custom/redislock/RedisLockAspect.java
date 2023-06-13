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
            /**
             * 如果此时出现极端情况，redis宕机，锁在事务外层无法回滚，如何解决？
             * org.redisson.client.WriteRedisConnectionException: Unable to write command into connection!
             *
             * redis部署集群，RedissonRedLock红锁采用了多节点上的锁控制和超时策略，可以有效解决单点故障、网络延迟以及时钟漂移等问题，降低了分布式系统多节点竞争出错的风险和误判率。
             * 总的来说，Redis 红锁提供了一种高可用、高并发的分布式锁解决方案，能够广泛应用于各种需要资源控制或者避免多个进程同时操作共享资源的场景中，例如：秒杀系统、抢购系统等等。
             *
             * 个人觉得特殊极端情况下最终还是得靠业务进行兜底，你们觉得呢？？？
             * 如果有牛逼点的处理方式，欢迎指出，谢谢 ^_^ ^_^ ^_^
             */
//            TimeUnit.SECONDS.sleep(10);

            // 释放锁
            lock.unlock();
        }
    }

}
