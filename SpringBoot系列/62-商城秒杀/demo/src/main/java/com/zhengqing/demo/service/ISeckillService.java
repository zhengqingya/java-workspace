package com.zhengqing.demo.service;

/**
 * <p>
 * 秒杀 服务类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
public interface ISeckillService {

    /**
     * 初始化数据
     */
    void initData(int stock);

    /**
     * 查询库存
     */
    Integer getStock();

    /**
     * 秒杀一_不加锁
     */
    void seckillSimple();

    /**
     * 秒杀二_加锁_ReentrantLock
     */
    void seckillReentrantLock();

    /**
     * 秒杀三_加锁_ReentrantLock_AOP
     */
    void seckillReentrantLockAop();

    /**
     * 秒杀四_加锁_DB悲观锁
     */
    void seckillDbPessimisticLock();

    /**
     * 秒杀五_加锁_DB乐观锁
     */
    void seckillDbOptimisticLock();

    /**
     * 秒杀六_加锁_Redis分布式锁
     */
    void seckillRedisLock();

    /**
     * 秒杀七_加锁_Redis分布式锁_AOP
     */
    void seckillRedisLockAop();

}
