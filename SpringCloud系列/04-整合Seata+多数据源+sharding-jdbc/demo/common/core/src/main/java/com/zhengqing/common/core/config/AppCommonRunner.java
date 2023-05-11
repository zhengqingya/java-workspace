package com.zhengqing.common.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 服务初始化之后，执行方法
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/5/22 19:29
 */
@Slf4j
@Component
public abstract class AppCommonRunner implements CommandLineRunner {

    public void appRun() {
//        log.info("《AppCommonRunner》: 服务初始化之后，执行方法 start...");

//        log.info("《AppCommonRunner》: 服务初始化之后，执行方法 end...");
    }

}
