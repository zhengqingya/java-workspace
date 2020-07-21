package com.zhengqing.demo;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * App定时任务$
 * </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/7/1$ 11:42$
 */
@Slf4j
@Component
@EnableScheduling
public class AppScheduledJobs {

    /**
     * 每3秒执行一次
     *
     * @return: void
     * @author : zhengqing
     * @date : 2020/7/1 11:44
     */
    @Scheduled(cron = "*/3 * * * * ?")
    public void test() {
        log.debug("==================================================================================");
        log.error("<<<<<< error Start: 【{}】 >>>>>>", LocalDateTime.now());
        log.warn("<<<<<< warn Start: 【{}】 >>>>>>", LocalDateTime.now());
        log.info("<<<<<< info Start: 【{}】 >>>>>>", LocalDateTime.now());
        log.debug("<<<<<< debug Start: 【{}】 >>>>>>", LocalDateTime.now());
    }

}
