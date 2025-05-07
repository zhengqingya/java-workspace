package com.zhengqing.demo.config;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p> Kafka监听器后处理器 </p>
 *
 * @author zhengqingya
 * @description 用于管理Kafka监听器的启用和停用
 * @date 2025/5/7 18:46
 */
@Slf4j
@Component
public class KafkaListenersPostProcessor implements BeanPostProcessor, ApplicationListener<ApplicationReadyEvent> {


    @Autowired
    private KafkaListenerEndpointRegistry registry;

    /**
     * 禁用的kafka组集合
     * eg：kafka.disabled-groupIds=simple,retry
     */
    @Value("#{'${kafka.disabled-groupIds:}'.split(',')}")
    private List<String> disabledGroupIds;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (CollUtil.isEmpty(disabledGroupIds)) {
            return;
        }
        try {
            if (registry == null) {
                log.warn("无法获取KafkaListenerEndpointRegistry，无法控制Kafka监听器");
            }
            for (MessageListenerContainer container : registry.getListenerContainers()) {
                String groupId = container.getGroupId();
                if (groupId == null) {
                    continue;
                }
                if (disabledGroupIds.contains(groupId)) {
                    log.info("禁用Kafka监听器: {}", groupId);
                    if (container.isRunning()) {
                        container.stop();
                    }
                }
            }
            log.info("Kafka监听器处理完成，禁用监听器：{}", disabledGroupIds);
        } catch (Exception e) {
            log.error("处理Kafka监听器时发生错误: ", e);
        }
    }
} 