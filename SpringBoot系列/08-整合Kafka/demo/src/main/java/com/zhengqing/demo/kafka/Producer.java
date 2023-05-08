
package com.zhengqing.demo.kafka;

import com.zhengqing.demo.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 生产者 - 发送消息 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/4/21 22:34
 */
@RestController
@RequestMapping("/api/")
public class Producer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @RequestMapping("send")
    public String send(String msg) {
        kafkaTemplate.send(Constants.KAFKA_TOPIC_HELLO, msg);
        return "SUCCESS";
    }

}
