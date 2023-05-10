package com.zhengqing.common.web.schedule;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 数据定时任务
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/4/12 0:48
 */
@Slf4j
@Component
public class Timer {

    /**
     * 每1小时执行一次
     */
    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void printCurrentTime() {
        log.debug("现在时间：【{}】", DateUtil.now());
    }

}
