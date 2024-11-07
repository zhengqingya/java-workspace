package com.zhengqing.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhengqing.demo.entity.User;
import com.zhengqing.demo.mapper.UserMapper;
import com.zhengqing.demo.util.TestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Value("${config.limit-num:3}")
    private Integer limitNum;
    private final UserMapper userMapper;
    private final TransactionTemplate transactionTemplate;
    private static final int MAX_PAGE_SIZE = 10000;

    public User getUserById(Integer id) {
        return userMapper.selectById(id);
    }

    public void updateUser(Integer id, String username) {
        userMapper.update(
                User.builder().username(username).build(),
                new LambdaQueryWrapper<User>().eq(User::getId, id)
        );
    }

    public String getNameById(Integer id) {
        return userMapper.selectNameById(id);
    }

    public void _01_test_no_return_value() {
        userMapper.executeNoReturnValue(1);
    }

    public void _02_test_spring_value() {
        System.out.println("limitNum：" + limitNum);
    }

    public void _03_test_static_method() {
        System.out.println("service test_static...");
        TestUtil.doNothing();
        System.out.println("getNowTime: " + TestUtil.getNowTime(1));
    }

    public void _04_test_transactionTemplate() {
        transactionTemplate.executeWithoutResult((action) -> {
            System.out.println("执行了...");
        });
    }

    public void _05_test_final_static_field() {
        System.out.println("MAX_PAGE_SIZE: " + MAX_PAGE_SIZE);
        System.out.println("UserService.MAX_PAGE_SIZE: " + UserService.MAX_PAGE_SIZE);
    }

}