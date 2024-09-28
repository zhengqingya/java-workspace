package com.zhengqing.demo.listener;

import cn.hutool.json.JSONUtil;
import com.zhengqing.demo.model.MqEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMqEventListener implements ApplicationListener<MqEvent> {

    @SneakyThrows
    @Override
    public void onApplicationEvent(MqEvent event) {
        log.info("RabbitMqEvent: {}", JSONUtil.toJsonStr(event));
    }
}