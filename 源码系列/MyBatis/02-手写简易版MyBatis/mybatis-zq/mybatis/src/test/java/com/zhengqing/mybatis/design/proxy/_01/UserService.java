package com.zhengqing.mybatis.design.proxy._01;

/**
 * <p> 用户service </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/21 17:08
 */
public interface UserService {

    Object selectList(String name);

    Object selectOne(String name);

}
