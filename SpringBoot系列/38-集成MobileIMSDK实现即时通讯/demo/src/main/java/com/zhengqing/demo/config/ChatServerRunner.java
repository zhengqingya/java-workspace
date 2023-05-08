package com.zhengqing.demo.config;


import com.zhengqing.demo.modules.chat.server.ServerLauncherImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * <p> 启动MobileIMSDK服务端 </p>
 *
 * @author : zhengqing
 * @description : run方法在SpringBoot服务启动之后会自动被调用
 * @date : 2020/2/6 11:28
 */
@Slf4j
@Component
@Order(value = 1)
public class ChatServerRunner implements CommandLineRunner {

    @Override
    public void run(String... strings) throws Exception {
        log.info("================= ↓↓↓↓↓↓ 启动MobileIMSDK服务端 ↓↓↓↓↓↓ =================");
        // 实例化后记得startup哦，单独startup()的目的是让调用者可以延迟决定何时真正启动IM服务
        final ServerLauncherImpl sli = new ServerLauncherImpl();
        // 启动MobileIMSDK服务端的Demo
        sli.startup();

        // 加一个钩子，确保在JVM退出时释放netty的资源
        Runtime.getRuntime().addShutdownHook(new Thread(sli::shutdown));
    }

}
