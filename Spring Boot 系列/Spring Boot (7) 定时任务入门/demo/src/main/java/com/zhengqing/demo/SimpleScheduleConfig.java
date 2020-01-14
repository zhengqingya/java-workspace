package com.zhengqing.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 *  <p>静态定时任务（基于注解）</p>
 *
 * @description : @Scheduled 除了支持灵活的参数表达式cron之外，还支持简单的延时操作，例如 fixedDelay ，fixedRate 填写相应的毫秒数即可。
*                 缺点: 当我们调整了执行周期的时候，需要重启应用才能生效。 -> 为了达到实时生效的效果，可以使用接口来完成定时任务。
 * @author : zhengqing
 * @date : 2019/11/4 18:10
 */
@Configuration //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling // 2.开启定时任务
public class SimpleScheduleConfig {
    /**
     * 3.添加定时任务
     * Cron表达式参数分别表示：
     *           秒（0~59） 例如0/5表示每5秒
     *           分（0~59）
     *           时（0~23）
     *           月的某天（0~31） 需计算
     *           月（0~11）
     *           周几（ 可填1-7 或 SUN/MON/TUE/WED/THU/FRI/SAT）
     */
    @Scheduled(cron = "0/5 * * * * ?")
    private void configureTasks() {
        System.err.println("执行定时任务1: " + LocalDateTime.now());
    }
}
