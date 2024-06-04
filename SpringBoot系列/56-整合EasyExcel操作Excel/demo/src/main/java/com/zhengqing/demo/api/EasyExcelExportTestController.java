package com.zhengqing.demo.api;

import com.zhengqing.demo.service.IDemoService;
import com.zhengqing.demo.service.IEasyExcelReadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p> 测试EasyExcel </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/1/24 17:37
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/easy-excel/test")
@Api(tags = {"导出"})
public class EasyExcelExportTestController {

    private final IEasyExcelReadService easyExcelReadService;
    private final IDemoService demoService;

    @ApiOperation("导出")
    @GetMapping(value = "/export")
    public void export(HttpServletResponse response) {

    }


    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    private final ThreadPoolExecutor threadPoolTaskExecutor = new ThreadPoolExecutor(
            corePoolSize, // corePoolSize: 线程池的核心线程数 -- 即便是线程池里没有任何任务，也会有corePoolSize个线程在候着等任务。
            corePoolSize + 1,  // maximumPoolSize: 最大线程数 -- 超过此数量，会触发拒绝策略。
            60, // keepAliveTime：线程的存活时间 -- 当线程池里的线程数大于corePoolSize时，如果等了keepAliveTime时长还没有任务可执行，则线程退出。
            TimeUnit.SECONDS, // unit：指定keepAliveTime的单位 -- 比如：秒：TimeUnit.SECONDS。
            new LinkedBlockingQueue<>(1000), // workQueue：一个阻塞队列，提交的任务将会被放到这个队列里。
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


}
