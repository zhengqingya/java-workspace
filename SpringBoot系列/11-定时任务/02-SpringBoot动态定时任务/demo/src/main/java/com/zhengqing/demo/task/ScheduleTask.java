package com.zhengqing.demo.task;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p> 定时任务执行 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/9/28 17:07
 */
@Slf4j
@Data
@Component
public class ScheduleTask implements SchedulingConfigurer {

    private void executeTask() {
        log.info("Task executed at: {}", DateUtil.now());
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(this::executeTask, triggerContext -> {
            try {
                // 动态修改执行策略 & 执行时间
                Date nextExecutionTime = getNextExecutionTimeByCronTrigger(triggerContext);
//                    Date nextExecutionTime = getNextExecutionTimeByPeriodicTrigger(triggerContext);
                log.debug("nextExecutionTime：{}", DateUtil.formatDateTime(nextExecutionTime));
                return nextExecutionTime;
            } catch (Exception e) {
                log.error("动态修改执行时间 error: ", e);
                throw new RuntimeException(e);
            }
        });
    }


    /**
     * cron表达式
     */
    private String cron = "0/10 * * * * ?"; // 10秒    每隔2分钟： 0 */2 * * * ?

    private Date getNextExecutionTimeByCronTrigger(TriggerContext triggerContext) {
        CronTrigger cronTrigger = new CronTrigger(cron);
        return cronTrigger.nextExecutionTime(triggerContext);
    }


    /**
     * 间隔时间，单位：毫秒
     */
    private Long timer = 1000L * 10; // 10秒

    private Date getNextExecutionTimeByPeriodicTrigger(TriggerContext triggerContext) {
        PeriodicTrigger periodicTrigger = new PeriodicTrigger(timer);
        return periodicTrigger.nextExecutionTime(triggerContext);
    }
}