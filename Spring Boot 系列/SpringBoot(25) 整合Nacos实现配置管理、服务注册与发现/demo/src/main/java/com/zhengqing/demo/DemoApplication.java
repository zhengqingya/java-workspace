package com.zhengqing.demo;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// 使用 @NacosPropertySource 加载 `dataId` 为 `application.yml` 的配置源，并开启自动更新
@NacosPropertySource(dataId = "application.yml", autoRefreshed = true)
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
