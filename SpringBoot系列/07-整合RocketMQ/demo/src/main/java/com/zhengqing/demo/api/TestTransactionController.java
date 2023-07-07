package com.zhengqing.demo.api;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description 事务消息是 Apache RocketMQ 提供的一种高级消息类型，支持在分布式场景下保障消息生产和本地事务的最终一致性
 * @date 2021/10/5 2:36 下午
 */
@Slf4j
@RestController
@RequestMapping("api/test/")
@RequiredArgsConstructor
@Api(tags = "测试API-事务消息（暂未测）")
public class TestTransactionController {

    private final RocketMQTemplate rocketMQTemplate;

    public static final String TOPIC = "transaction";
    // 主题+tag，中间用“:”分隔，主要是用于消息的过滤，比如说在消费的时候，只消费tag1标签下的消息
    private static final String TOPIC_TAG = TOPIC.concat(":tag1");

    @ApiOperation("事务消息")
    @PostMapping("/sendMessageInTransaction")
    public Object sendMessageInTransaction() {
        String msgContent = DateUtil.now();
        log.info("[生产者] 发送消息：{}", msgContent);
        String id = IdUtil.getSnowflakeNextIdStr();
        Message<String> message = MessageBuilder.withPayload(msgContent)
                .setHeader("KEYS", id)
                // 设置事务ID
                .setHeader(RocketMQHeaders.TRANSACTION_ID, "KEY_" + id)
                .build();
        TransactionSendResult transactionSendResult = this.rocketMQTemplate.sendMessageInTransaction(TOPIC_TAG, message, null);
        return transactionSendResult;
    }

    /**
     * 监听器
     */
    @Slf4j
    @RocketMQTransactionListener
    public static class MyRocketMQLocalTransactionListener implements RocketMQLocalTransactionListener {
        private AtomicInteger transactionIndex = new AtomicInteger(0);
        private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<String, Integer>();

        /**
         * 执行事务
         */
        @Override
        public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            //事务ID
            String transId = (String) msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);

            int value = this.transactionIndex.getAndIncrement();
            int status = value % 3;
            assert transId != null;
            this.localTrans.put(transId, status);

            if (status == 0) {
                log.info("success");
                // 成功，提交事务
                return RocketMQLocalTransactionState.COMMIT;
            }

            if (status == 1) {
                log.info("failure");
                // 失败，回滚事务
                return RocketMQLocalTransactionState.ROLLBACK;
            }

            log.info("unknown");
            // 中间状态
            return RocketMQLocalTransactionState.UNKNOWN;
        }

        /**
         * 检查事务状态
         */
        @Override
        public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
            String transId = (String) msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
            RocketMQLocalTransactionState retState = RocketMQLocalTransactionState.COMMIT;
            Integer status = this.localTrans.get(transId);
            if (null != status) {
                switch (status) {
                    case 0:
                        retState = RocketMQLocalTransactionState.COMMIT;
                        break;
                    case 1:
                        retState = RocketMQLocalTransactionState.ROLLBACK;
                        break;
                    case 2:
                        retState = RocketMQLocalTransactionState.UNKNOWN;
                        break;

                    default:
                        break;
                }
            }
            log.info("msgTransactionId:{},TransactionState:{},status:{}", transId, retState, status);
            return retState;
        }
    }


}
