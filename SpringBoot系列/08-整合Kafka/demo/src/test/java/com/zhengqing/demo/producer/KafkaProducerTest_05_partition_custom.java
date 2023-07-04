package com.zhengqing.demo.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;

import java.util.Map;
import java.util.Properties;

/**
 * <p> 生产者发送消息 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/7/4 15:48
 */
public class KafkaProducerTest_05_partition_custom {


    /**
     * 自定义分区器
     */
    public static class MyPartitioner implements Partitioner {
        /**
         * 返回信息对应的分区
         *
         * @param topic      主题
         * @param key        消息的 key
         * @param keyBytes   消息的 key 序列化后的字节数组
         * @param value      消息的 value
         * @param valueBytes 消息的 value 序列化后的字节数组
         * @param cluster    集群元数据可以查看分区信息
         * @return
         */
        @Override
        public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
            // 获取消息
            String msgValue = value.toString();
            // 创建 partition
            int partition;
            // 判断消息是否包含xx值
            if (msgValue.contains("zhengqingya")) {
                partition = 0;
            } else {
                partition = 1;
            }
            // 返回分区号
            return partition;
        }

        // 关闭资源
        @Override
        public void close() {
        }

        // 配置方法
        @Override
        public void configure(Map<String, ?> configs) {
        }
    }


    @Test
    public void test() throws Exception {
        this.updateLogLevel();
        // 1. 创建 kafka 生产者的配置对象
        Properties properties = new Properties();
        // 2. 给 kafka 配置对象添加配置信息：bootstrap.servers
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        // key,value 序列化（必须）：key.serializer，value.serializer
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 添加自定义分区器
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, MyPartitioner.class.getName());
        // 3. 创建 kafka 生产者对象
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        // 4. 调用 send 方法,发送消息
        for (int i = 0; i < 5; i++) {
            kafkaProducer.send(new ProducerRecord<>("my-topic", "zhengqingya: " + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        // 没有异常,输出信息到控制台
                        System.out.println("主题：" + recordMetadata.topic() + "->" + "分区：" + recordMetadata.partition());
                    } else {
                        // 出现异常打印
                        e.printStackTrace();
                    }
                }
            });
        }

        // 5. 关闭资源
        kafkaProducer.close();
    }

    private void updateLogLevel() {
        LoggingSystem loggingSystem = LoggingSystem.get(LoggingSystem.class.getClassLoader());
        loggingSystem.setLogLevel("org.apache.kafka", LogLevel.WARN);
    }

}
