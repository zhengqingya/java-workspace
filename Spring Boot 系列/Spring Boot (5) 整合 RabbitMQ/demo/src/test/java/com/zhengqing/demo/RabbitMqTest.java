package com.zhengqing.demo;

import com.zhengqing.demo.modules.rabbitmq.producer.MsgSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMqTest {

    @Autowired
    private MsgSender msgSender;

    @Test
    public void helloWorld() throws Exception {
        msgSender.send();
    }

}
