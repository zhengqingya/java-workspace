package com.zhengqing.demo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.time.LocalDateTime;

/**
 *  <p> 动态定时任务（基于接口） </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/11/4 18:40
 */
@Configuration
@EnableScheduling
public class ScheduleConfigDynamic implements SchedulingConfigurer {

    @Autowired
    CronMapper cronMapper;

    /**
     * 执行定时任务: 可打开Navicat进行动态修改执行周期  ex:将执行周期修改为每1秒执行一次
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 添加TriggerTask -> 目的: 循环读取我们在数据库设置好的执行周期，以及执行相关定时任务的内容
        taskRegistrar.addTriggerTask(
                // 1.添加任务内容(Runnable)
                () -> System.out.println("执行定时任务2: " + LocalDateTime.now().toLocalTime()),
                // 2.设置执行周期(Trigger)
                triggerContext -> {
                    // 2.1 从数据库获取执行周期
                    String cron = cronMapper.getCron();
                    // 2.2 合法性校验.
                    if (StringUtils.isEmpty(cron)) {
                        // Omitted Code ..
                    }
                    // 2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }

}
