package com.zhengqing.demo.service.impl;

import cn.hutool.core.lang.Assert;
import com.zhengqing.demo.custom.redislock.RedisLock;
import com.zhengqing.demo.custom.reentrantlock.ReentrantLock;
import com.zhengqing.demo.service.ISeckillService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * <p>
 * 秒杀 服务实现类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SeckillServiceImpl implements ISeckillService {

    private final JdbcTemplate jdbcTemplate;
    private final Lock reentrantLock = new java.util.concurrent.locks.ReentrantLock(true);
    private final RedissonClient redissonClient;

    @Override
    public void initData(int stock) {
        this.jdbcTemplate.execute(
                "DROP TABLE IF EXISTS `t_seckill`;\n" +
                        "\n" +
                        "CREATE TABLE `t_seckill`\n" +
                        "(\n" +
                        "    `id`    int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',\n" +
                        "    `stock` int(11) NOT NULL COMMENT '库存',\n" +
                        "    `version` int(11) NOT NULL DEFAULT '1' COMMENT '版本号',\n" +
                        "    PRIMARY KEY (`id`) USING BTREE\n" +
                        ") ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '秒杀表' ROW_FORMAT = Dynamic;\n" +
                        "\n" +
                        "INSERT INTO `t_seckill` VALUES (1, " + stock + ", 1);"
        );
    }

    @Override
    public Integer getStock() {
        return this.jdbcTemplate.queryForObject("SELECT stock FROM t_seckill WHERE id = 1", Integer.class);
    }

    @SneakyThrows(Exception.class)
    private void buy() {
        Assert.isTrue(this.getStock() > 0, "库存不足！");

        // 放大并发问题 100毫秒
//        TimeUnit.MILLISECONDS.sleep(100);

        int updateNum = this.jdbcTemplate.update("UPDATE t_seckill SET stock = stock-1 WHERE id = 1");
        Assert.isTrue(updateNum == 1, "库存不足！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void seckillSimple() {
        /**
         *       并发同时查询库存  操作减库存  最终结果
         * 事务A： 1         =》  -1      =》 0
         * 事务B： 1         =》  -1      =》 由于行锁，等到事务A先执行完，再扣减库存 == -1
         */
        this.buy();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void seckillReentrantLock() {
        /**
         * 代码执行流程：开启事务 =》加锁 =》执行业务 =》释放锁 =》提交事务
         * 问题：锁在事务提交之前释放了...
         *
         *       查询库存     操作减库存   锁释放，再提交事务   最终结果
         * 事务A： 1         =》  -1                       =》 0
         * 事务B： 1         =》  -1                       =》 -1
         *
         * tips: 事务B在事务A未提交事务之前读取到db旧值 1 导致超卖
         */
        try {
            this.reentrantLock.lock();
            System.out.println(Thread.currentThread().getName() + ": 进入锁..." + this.getStock());
            this.buy();
        } catch (Exception e) {
            log.error("异常：", e);
        } finally {
            System.out.println(Thread.currentThread().getName() + ": 释放锁..." + this.getStock());
            this.reentrantLock.unlock();
        }
    }

    @Override
    @ReentrantLock
    @Transactional(rollbackFor = Exception.class)
    public void seckillReentrantLockAop() {
        this.buy();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void seckillDbPessimisticLock() {
        /**
         * 悲观锁（Pessimistic Lock），顾名思义，即默认认为并发情况下数据会被其他事务修改，因此在访问共享资源之前，就对其进行加锁，以防止并发操作导致的数据不一致或丢失。
         */
        Integer stock = this.jdbcTemplate.queryForObject("SELECT stock FROM t_seckill WHERE id = 1 FOR UPDATE", Integer.class);
        Assert.isTrue(stock > 0, "库存不足！");

        int updateNum = this.jdbcTemplate.update("UPDATE t_seckill SET stock = stock-1 WHERE id = 1");
        Assert.isTrue(updateNum == 1, "库存不足！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void seckillDbOptimisticLock() {
        /**
         * 乐观锁（Optimistic Lock）是一种常见的并发控制方式，通常用于在高并发场景下保证数据的一致性和完整性。
         * 其基本思想是，在业务逻辑处理之前，先从数据库中读取需要修改的数据，并且在读取时不对记录加锁。然后在提交数据更新操作时，再次检查是否有其他事务修改了该数据。
         * 如果该数据未被其他事务修改，则可以执行正常的更新操作；如果数据已经被其他事务修改，则根据具体实现方式进行相应的冲突处理（如抛出异常、重试等），以确保数据最终能够达到正确和一致的状态。
         *
         * 具体实现方法包括但不限于：
         *      1. 在数据表中添加一个版本号字段（或时间戳字段），每次修改时将此字段+1（或更新为当前时间戳），提交时要求更新语句中同时满足原始版本号（或时间戳）与数据库中存储的版本号（或时间戳）匹配；
         *         eg: UPDATE t_seckill SET stock = stock-1,version = version+1 WHERE id = 1 AND version = 1;
         *      2. 利用CAS（Compare And Set）机制等原子操作，避免多个线程同时更新同一数据。
         *
         * 乐观锁相对于悲观锁而言，具有低阻塞、低死锁等优势，但同时也更容易出现冲突和竞争，需要开发人员针对具体业务场景进行实际评估和优化。
         *
         * 下面使用条件限制实现乐观锁，`stock-1 >= 0`，这个情景适合不用版本号，更新时做数据安全校验，适合库存模型，性能更高。
         */
        int updateNum = this.jdbcTemplate.update("UPDATE t_seckill SET stock = stock-1 WHERE id = 1 AND stock-1 >= 0");
        Assert.isTrue(updateNum == 1, "库存不足！");
    }

    @Override
    @SneakyThrows(Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public void seckillRedisLock() {
        /**
         * 问题：和ReentrantLock类似 锁在事务提交之前释放了... 超卖
         */
        RLock redisLock = this.redissonClient.getLock("stock:lock:1");
        try {
            redisLock.lock();
            this.buy();
        } catch (Exception e) {
            throw e;
        } finally {
            redisLock.unlock();
        }
        // 放大并发问题 100毫秒
        TimeUnit.MILLISECONDS.sleep(100);
    }

    @Override
    @RedisLock(key = "stock:lock:1")
    @Transactional(rollbackFor = Exception.class)
    public void seckillRedisLockAop() {
        this.buy();
    }

}
