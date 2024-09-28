package com.zhengqing.demo.api;


import cn.hutool.core.date.DateUtil;
import com.zhengqing.demo.task.ScheduleTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/5 2:36 下午
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
@Api(tags = "测试api")
public class TestController {

    private final ScheduleTask scheduleTask;

    @GetMapping("time")
    @ApiOperation("time")
    public String time() {
        log.info("time: {}", DateUtil.date());
        return DateUtil.date().toString();
    }

    @ApiOperation("动态修改定时任务cron")
    @GetMapping("updateCron") // http://127.0.0.1/updateCron?cron=0/1 * * * * ?
    public String updateCron(String cron) {
        log.info("new cron :{}", cron);
        scheduleTask.setCron(cron);
        return "ok";
    }

    @ApiOperation("动态修改定时任务执行时间")
    @GetMapping("updateTimer") // http://127.0.0.1/updateTimer?timer=3000
    public String updateTimer(Long timer) {
        log.info("new timer: {}", timer);
        scheduleTask.setTimer(timer);
        return "ok";
    }

}
