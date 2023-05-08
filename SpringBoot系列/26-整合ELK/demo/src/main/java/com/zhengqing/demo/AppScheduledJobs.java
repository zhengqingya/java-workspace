package com.zhengqing.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * <p>
 * App定时任务
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/8/2 14:01
 */
@Slf4j
@Component
@EnableScheduling
public class AppScheduledJobs {

    /**
     * 每5秒执行一次
     *
     * @return void
     * @author zhengqingya
     * @date 2021/8/2 8:10 下午
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void test() {
        log.debug("==================================================================================");
        log.error("<<<<<< error Start: 【{}】 >>>>>>", LocalDateTime.now());
        log.warn("<<<<<< warn Start: 【{}】 >>>>>>", LocalDateTime.now());
        log.info("<<<<<< info Start: 【{}】 >>>>>>", LocalDateTime.now());
        log.debug("<<<<<< debug Start: 【{}】 >>>>>>", LocalDateTime.now());
    }

}
