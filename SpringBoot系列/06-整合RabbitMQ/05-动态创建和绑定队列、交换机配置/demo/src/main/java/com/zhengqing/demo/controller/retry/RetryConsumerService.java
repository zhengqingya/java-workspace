package com.zhengqing.demo.controller.retry;

import cn.hutool.core.date.DateTime;
import com.zhengqing.demo.dynamic.service.impl.AbsConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p> 消费者 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/7/10 15:59
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RetryConsumerService extends AbsConsumerService<String> {

    @Override
    public void onConsumer(String data) throws Exception {
        log.info("{} [消费者] 接收消息: {}", DateTime.now(), data);
//        int a = 1 / 0;
        throw new Exception("异常了...");
    }

}
