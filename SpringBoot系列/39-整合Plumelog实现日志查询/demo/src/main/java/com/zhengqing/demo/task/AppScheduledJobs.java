package com.zhengqing.demo.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <p> App定时任务 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/1/12 10:18
 */
@Slf4j
@Component
@EnableScheduling
public class AppScheduledJobs {

    /**
     * 每3秒执行一次
     */
    @Scheduled(cron = "*/1 * * * * ?")
    public void test() {
        log.debug("==================================================================================");
        log.error("<<<<<< error Start: 【{}】 >>>>>>", DateUtil.now());
        log.warn("<<<<<< warn Start: 【{}】 >>>>>>", DateUtil.now());
        log.info("<<<<<< info Start: 【{}】 >>>>>>", DateUtil.now());
        log.debug("<<<<<< debug Start: 【{}】 >>>>>>", DateUtil.now());
        log.debug(HttpUtil.get("http://127.0.0.1/test"));
    }

}
