package com.zhengqing.demo.api;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.zhengqing.demo.model.MqEvent;
import io.swagger.annotations.Api;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
@Api(tags = "SpringEvent")
public class SpringEventController {

    private final JdbcTemplate jdbcTemplate;
    /**
     * ApplicationEventPublisher 或 ApplicationContext 都可以发布事件
     */
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ApplicationContext applicationContext;

    @GetMapping("/spring-event")
    @SneakyThrows(Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public Object springEvent(@RequestParam Integer num) {
        jdbcTemplate.execute("TRUNCATE TABLE t_test;");
        if (num > 0) {
            log.info("1: 插入了...");
            jdbcTemplate.execute(StrUtil.format("INSERT INTO `t_test` (`name`) VALUES ('doInTransaction1-{}');", DateUtil.now()));
        }
        applicationEventPublisher.publishEvent(MyEvent.builder().id(num).name(DateUtil.now()).build());
        return "ok:" + DateUtil.now();
    }


    @Order(2)
    @EventListener // 同步执行
    @Transactional(rollbackFor = Exception.class) // 无父事务 这里的子事务也可单独有效
    public void handleEvent1(MyEvent myEvent) {
        log.info("handleEvent1: {}", JSONUtil.toJsonStr(myEvent));
        jdbcTemplate.execute(StrUtil.format("INSERT INTO `t_test` (`name`) VALUES ('handleEvent1-{}');", DateUtil.now()));
        if (myEvent.getId() == 1) {
            System.out.println(1 / 0);
        }
        log.info("handleEvent1 end ...");
    }


    @Order(1) // 要比 handleEvent1 先执行，如果 handleEvent1 先执行 报错了，则 handleEvent2 也不会执行
    @Async // 异步执行
    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void handleEvent2(MyEvent myEvent) {
        log.info("handleEvent2: {}", JSONUtil.toJsonStr(myEvent));
        jdbcTemplate.execute(StrUtil.format("INSERT INTO `t_test` (`name`) VALUES ('handleEvent2-{}');", DateUtil.now()));
        log.info("handleEvent2 end ...");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MyEvent {
        private Integer id;
        private String name;
    }

    @GetMapping("/spring-event-2")
    @SneakyThrows(Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public Object springEvent2() {
        log.debug("hello...");
        applicationContext.publishEvent(new MqEvent(this, 1, DateUtil.now()));
        return "ok:" + DateUtil.now();
    }
}



