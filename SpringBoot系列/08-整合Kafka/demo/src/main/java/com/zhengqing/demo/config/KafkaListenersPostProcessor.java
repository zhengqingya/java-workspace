package com.zhengqing.demo.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
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
     * eg：kafka.disabled-groupIds=simple-local,retry-local
     *
     * <p>
     * 真正停用之后会打印日志如下，可以通过搜索此日志（ 需要开启日志：logging.level.org.springframework.kafka=INFO ）
     * 或者 看下kafka可视化界面中消费者列表中是否存在此组，不存在则标识停用成功。
     * 2025-05-08 11:19:10.305  INFO 17332 --- [tainer#10-3-C-1] o.s.k.l.KafkaMessageListenerContainer    : simple-local: Consumer stopped
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
                    log.info("topics:{} 禁用Kafka监听器: {}", JSONUtil.toJsonStr(container.getContainerProperties().getTopics()), groupId);
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