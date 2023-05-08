package com.zhengqing.demo.service;

/**
 * <p>
 * 测试demo业务 服务类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
public interface IBusinessService {


    /**
     * 服务重试
     *
     * @param msg 消息
     * @return 结果
     * @author zhengqingya
     * @date 2022/4/6 10:20
     */
    String retry(String msg);

}
