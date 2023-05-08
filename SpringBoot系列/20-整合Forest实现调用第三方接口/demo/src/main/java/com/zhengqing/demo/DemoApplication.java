package com.zhengqing.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.thebeastshop.forest.springboot.annotation.ForestScan;

// forest扫描远程接口所在的包名
@ForestScan(basePackages = "com.zhengqing.demo.rpc.client")
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
