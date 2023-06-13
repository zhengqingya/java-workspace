package com.zhengqing.demo.api;

import cn.hutool.core.util.StrUtil;
import com.zhengqing.demo.enums.SeckillModeEnum;
import com.zhengqing.demo.service.ISeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;

/**
 * <p>
 * 秒杀 接口
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
@Slf4j
@RestController
@RequestMapping("/api/seckill")
@RequiredArgsConstructor
@Api(tags = {"秒杀接口"})
public class SeckillController {

    private final ISeckillService seckillService;
    private static int corePoolSize = Runtime.getRuntime().availableProcessors() * 3;
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(
            corePoolSize, // corePoolSize: 线程池的核心线程数 -- 即便是线程池里没有任何任务，也会有corePoolSize个线程在候着等任务。
            corePoolSize + 1,  // maximumPoolSize: 最大线程数 -- 超过此数量，会触发拒绝策略。
            2, // keepAliveTime：线程的存活时间 -- 当线程池里的线程数大于corePoolSize时，如果等了keepAliveTime时长还没有任务可执行，则线程退出。
            TimeUnit.SECONDS, // unit：指定keepAliveTime的单位 -- 比如：秒：TimeUnit.SECONDS。
            new LinkedBlockingQueue<>(3), // workQueue：一个阻塞队列，提交的任务将会被放到这个队列里。
            Executors.defaultThreadFactory(), // threadFactory：线程工厂，用来创建线程 -- 主要是为了给线程起名字，默认工厂的线程名字：pool-1-thread-3。
            /**
             * handler：拒绝策略 -- 当线程池里线程被耗尽，且队列也满了的时候会调用。默认拒绝策略为AbortPolicy。即：不执行此任务，而且抛出一个运行时异常
             * 四种拒绝策略:
             * 1. AbortPolicy：线程满了还有线程要进入则不做处理，直接抛出异常
             * 2. CallerRunsPolicy： 超过的任务将由调用者线程去执行，比如这里是交给main主线程执行
             * 3. DiscardPolicy：队列满了，直接丢掉新来的任务，不会抛出异常
             * 4. DiscardOldestPolicy()：相对 DiscardPolicy 它丢弃的不是最新提交的，而是队列中存活时间最长的，=> 腾出空间给新提交的任务
             */
            new ThreadPoolExecutor.CallerRunsPolicy());

    @PostMapping("")
    @ApiOperation("秒杀")
    @SneakyThrows(Exception.class)
    public String seckill(@ApiParam(value = "秒杀模式", allowableValues = "秒杀一_不加锁,秒杀二_加锁_ReentrantLock,秒杀三_加锁_ReentrantLock_AOP,秒杀四_加锁_DB悲观锁,秒杀五_加锁_DB乐观锁,秒杀六_加锁_Redis分布式锁,秒杀七_加锁_Redis分布式锁_AOP")
                          @RequestParam String mode,
                          @ApiParam(value = "库存", example = "10") @RequestParam int stock,
                          @ApiParam(value = "秒杀人数", example = "20") @RequestParam int userNum) {
        // 1、初始化数据
        this.seckillService.initData(stock);

        // 2、秒杀
        final CountDownLatch countDownLatch = new CountDownLatch(userNum);
        for (int i = 0; i < userNum; i++) {
            this.executor.execute(() -> {
                try {
//                System.out.println("线程: " + Thread.currentThread().getName());
                    switch (SeckillModeEnum.valueOf(mode)) {
                        case 秒杀一_不加锁:
                            this.seckillService.seckillSimple();
                            break;
                        case 秒杀二_加锁_ReentrantLock:
                            this.seckillService.seckillReentrantLock();
                            break;
                        case 秒杀三_加锁_ReentrantLock_AOP:
                            this.seckillService.seckillReentrantLockAop();
                            break;
                        case 秒杀四_加锁_DB悲观锁:
                            this.seckillService.seckillDbPessimisticLock();
                            break;
                        case 秒杀五_加锁_DB乐观锁:
                            this.seckillService.seckillDbOptimisticLock();
                            break;
                        case 秒杀六_加锁_Redis分布式锁:
                            this.seckillService.seckillRedisLock();
                            break;
                        case 秒杀七_加锁_Redis分布式锁_AOP:
                            this.seckillService.seckillRedisLockAop();
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    log.error("异常：", e);
                } finally {
                    // -1
                    countDownLatch.countDown();
                }
            });
        }

        // 等待所有人完成任务再GO ↓↓↓
        countDownLatch.await();

        // 3、查询库存
        return StrUtil.format("总库存：{} \n秒杀人数：{} \n剩余库存：{}", stock, userNum, this.seckillService.getStock());
    }

}
