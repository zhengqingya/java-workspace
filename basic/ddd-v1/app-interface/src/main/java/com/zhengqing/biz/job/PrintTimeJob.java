package com.zhengqing.biz.job;

import cn.hutool.core.date.DateUtil;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class PrintTimeJob {
    @Scheduled(fixedRate = 3000)
    public void executeTask() {
        // 在这里编写需要执行的定时任务逻辑
        System.out.println("每3秒执行一次的定时任务: " + DateUtil.now());
    }
}
