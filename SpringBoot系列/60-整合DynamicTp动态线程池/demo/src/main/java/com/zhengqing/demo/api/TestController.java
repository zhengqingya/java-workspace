package com.zhengqing.demo.api;


import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.dromara.dynamictp.core.DtpRegistry;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/5 2:36 下午
 */
@Slf4j
@RestController
@RequestMapping("/api")
@Api(tags = "测试api")
@RefreshScope
public class TestController {

    //    @Value("${test}")
//    private String test;

    // 使用方式1
    @Resource
    private ThreadPoolExecutor dtpExecutorTest;

    @GetMapping("test")
    @ApiOperation("test")
    public Object test() {
        log.info("time: {}", DateUtil.date());
//        System.out.println(DtpRegistry.listAllExecutors());
        // 使用方式2
//        DtpExecutor dtpExecutorTest = DtpRegistry.getDtpExecutor("dtpExecutorTest");
        for (int i = 0; i < 100; i++) {
            this.dtpExecutorTest.execute(() -> {
                log.info("dynamic-tp-test");
            });
        }
        return DtpRegistry.listAllExecutorNames();
    }

}
