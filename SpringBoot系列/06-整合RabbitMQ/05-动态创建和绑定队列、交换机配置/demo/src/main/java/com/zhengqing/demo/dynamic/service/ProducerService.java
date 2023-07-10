package com.zhengqing.demo.dynamic.service;

/**
 * <p> 生产者接口 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/7/10 15:59
 */
public interface ProducerService {

    /**
     * 发送消息
     *
     * @param message 消息
     */
    void send(Object message);

}
