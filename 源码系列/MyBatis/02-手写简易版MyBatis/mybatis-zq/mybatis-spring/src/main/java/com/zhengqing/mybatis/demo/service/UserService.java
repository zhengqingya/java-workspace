package com.zhengqing.mybatis.demo.service;


import com.zhengqing.mybatis.demo.entity.User;

/**
 * <p> 用户service </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/5/5 04:03
 */
public interface UserService {

    User findOne(Integer id);

    void save(User user);

}
