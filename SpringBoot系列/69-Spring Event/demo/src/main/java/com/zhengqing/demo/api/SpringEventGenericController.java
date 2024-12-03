package com.zhengqing.demo.api;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
@Api(tags = "SpringEvent")
public class SpringEventGenericController {

    private final ApplicationEventPublisher applicationEventPublisher;

    @ApiOperation("泛型测试1")
    @GetMapping("/spring-event-generic-01")
    public Object generic_01() {
        log.debug("generic_01...");
        applicationEventPublisher.publishEvent(GenericEvent.builder()
                .id(1)
                .name("test")
                .data(MyEvent1.builder().userId("1").userName("zhengqingya").build())
                .build());
        return "ok:" + DateUtil.now();
    }

    @ApiOperation("泛型测试2")
    @GetMapping("/spring-event-generic-02")
    public Object generic_02() {
        log.debug("generic_02...");
        applicationEventPublisher.publishEvent(GenericEvent.builder()
                .id(1)
                .name("test")
                .data(MyEvent2.builder().orderId("1").payMoney("99.9").build())
                .build());
        return "ok:" + DateUtil.now();
    }

    @Order(2)
    @EventListener // 同步执行
    public void handleEvent1(GenericEvent<MyEvent1> genericEvent) {
        log.info("handleEvent1: {}", JSONUtil.toJsonStr(genericEvent));
    }


    @Order(2)
    @EventListener
    public void handleEvent2_1(GenericEvent<MyEvent2> genericEvent) {
        log.info("handleEvent2_1: {}", JSONUtil.toJsonStr(genericEvent));
    }

    @Order(1) // 要比 handleEvent2_1 先执行，如果 handleEvent2_2 先执行 报错了，则 handleEvent2_1 也不会执行
    @EventListener
    public void handleEvent2_2(GenericEvent<MyEvent2> genericEvent) {
        log.info("handleEvent2_2: {}", JSONUtil.toJsonStr(genericEvent));
//        int i = 1 / 0;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GenericEvent<T> implements ResolvableTypeProvider {
        private Integer id;
        private String name;
        private T data;

        @Override
        public ResolvableType getResolvableType() {
            return ResolvableType.forClassWithGenerics(GenericEvent.class, ResolvableType.forInstance(data));
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MyEvent1 {
        private String userId;
        private String userName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MyEvent2 {
        private String orderId;
        private String payMoney;
    }

}



