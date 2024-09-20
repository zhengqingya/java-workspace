package com.zhengqing.demo;

import com.zhengqing.demo.netty.common.protocol.SerializerStrategy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.Serializable;

/**
 * <p> 序列化测试 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/9/17 2:16
 */
@Slf4j
public class TestSerializer {

    final User user = User.builder().id(1).name("zq").build();

    @Test
    public void test_java() throws Exception {
        SerializerStrategy.Algorithm serializerAlgorithm = SerializerStrategy.Algorithm.Java;
        byte[] bytes = serializerAlgorithm.serialize(user);
        User deserialize = serializerAlgorithm.deserialize(bytes, User.class);
        System.out.println(deserialize);
    }

    @Test
    public void test_java_hutool() throws Exception {
        SerializerStrategy.Algorithm serializerAlgorithm = SerializerStrategy.Algorithm.Java_hutool;
        byte[] bytes = serializerAlgorithm.serialize(user);
        User deserialize = serializerAlgorithm.deserialize(bytes, User.class);
        System.out.println(deserialize);
    }


    @Test
    public void test_json() throws Exception {
        SerializerStrategy.Algorithm serializerAlgorithm = SerializerStrategy.Algorithm.Json;
        byte[] bytes = serializerAlgorithm.serialize(user);
        User deserialize = serializerAlgorithm.deserialize(bytes, User.class);
        System.out.println(deserialize);
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaseEntity implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer index;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User extends BaseEntity implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer id;
        private String name;
    }

}
