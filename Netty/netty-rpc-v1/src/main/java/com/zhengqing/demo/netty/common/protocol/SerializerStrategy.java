package com.zhengqing.demo.netty.common.protocol;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * <p> 序列化算法 </p>
 *
 * @author zhengqingya
 * @description 序列化 & 反序列化算法
 * @date 2024/9/16 23:48
 */
public interface SerializerStrategy {

    <T> byte[] serialize(T object) throws Exception;

    <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception;

    enum Algorithm implements SerializerStrategy {

        Java {
            @Override
            public <T> byte[] serialize(T object) throws Exception {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(object);
                return byteArrayOutputStream.toByteArray();
            }

            @Override
            public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
                return (T) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
            }
        },
        // 有父类的情况下，hutool反序列化异常：cn.hutool.core.io.IORuntimeException: InvalidClassException: Unauthorized deserialization attempt; com.zhengqing.demo.TestSerializer$BaseEntity
        Java_hutool {
            @Override
            public <T> byte[] serialize(T object) throws Exception {
                return ObjectUtil.serialize(object);
            }

            @Override
            public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
                return ObjectUtil.deserialize(bytes, clazz);
            }
        },

        Json {
            @Override
            public <T> byte[] serialize(T object) throws Exception {
                return JSONUtil.toJsonStr(object).getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
                return JSONUtil.toBean(new String(bytes, StandardCharsets.UTF_8), clazz);
            }
        },
        Protobuf {
            @Override
            public <T> byte[] serialize(T object) throws Exception {
                return null;
            }

            @Override
            public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
                return null;
            }
        }
    }
}