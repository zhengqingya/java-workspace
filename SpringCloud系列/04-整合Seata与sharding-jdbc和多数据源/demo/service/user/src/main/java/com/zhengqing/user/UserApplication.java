package com.zhengqing.user;

import com.zhengqing.common.base.constant.AppConstant;
import com.zhengqing.common.base.constant.ServiceConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = {ServiceConstant.SERVICE_BASE_PACKAGE})
@EnableDiscoveryClient // 开启服务注册发现功能
@EnableScheduling // 开启任务调度
@EnableTransactionManagement
@EnableFeignClients(basePackages = {AppConstant.RPC_BASE_PACKAGE}) // 开启Feign并扫描Feign客户端
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
