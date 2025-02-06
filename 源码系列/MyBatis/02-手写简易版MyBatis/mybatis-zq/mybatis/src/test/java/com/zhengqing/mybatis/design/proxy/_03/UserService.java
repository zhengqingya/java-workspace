package com.zhengqing.mybatis.design.proxy._03;

/**
 * <p> 用户service </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/21 17:08
 */
public class UserService {

    public Object selectList(String name) {
        System.out.println("查询用户列表");
        return "ok";
    }

}
