package com.zhengqing.mytest.config;

import cn.hutool.core.date.DateTime;
import com.zhengqing.mytest.util.TestUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ConditionalOnClass({TestUtil.class}) // 条件注解，存在指定类的情况下生效，eg：依赖第三方xx才生效
@EnableConfigurationProperties({BaseProperty.class})
@ConditionalOnProperty(
        value = {"base.enable"},
        havingValue = "true",
        // true表示缺少此配置属性时也会加载该bean
        matchIfMissing = true
)
public class MyAutoConfiguration {

    static {
        System.err.println("MyAutoConfiguration: " + DateTime.now());
    }

    @Bean
//    @ConditionalOnMissingBean(BaseProperty.class)
    public TestUtil testUtil(BaseProperty baseProperty) {
        TestUtil emailService = new TestUtil(baseProperty);
        return emailService;
    }

}
