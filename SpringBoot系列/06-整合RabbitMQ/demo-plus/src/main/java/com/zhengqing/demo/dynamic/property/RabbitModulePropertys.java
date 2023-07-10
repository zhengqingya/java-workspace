package com.zhengqing.demo.dynamic.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * <p> mq配置属性 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/7/8 10:34
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitModulePropertys {

    /**
     * 动态创建和绑定队列、交换机的配置
     */
    private List<RabbitModuleProperty> moduleList;

}
