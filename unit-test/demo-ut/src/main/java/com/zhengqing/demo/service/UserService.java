package com.zhengqing.demo.service;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.zhengqing.demo.entity.User;
import com.zhengqing.demo.mapper.UserMapper;
import com.zhengqing.demo.util.TestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Value("${config.limit-num:3}")
    private Integer limitNum;
    private final UserMapper userMapper;
    private final TransactionTemplate transactionTemplate;
    private static final int MAX_PAGE_SIZE = 10000;
    private static int INIT_NUM = 1;
    private final ExecutorService executorService;

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

    public void _02_test_static_field_value() {
        System.out.println("INIT_NUM：" + INIT_NUM);
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

    public void _06_test_mock_new_instance() {
        User user = new User(111);
        System.out.println("_06_test_mock_new_instance id: " + user.getId());
    }

    public void _100_CompletableFuture() {
        // ExecutorService executorService = Executors.newFixedThreadPool(5); // 这种线程池mock正常运行
        // private final ExecutorService executorService; 这种注入的方式mock会一直转圈儿...

        // 批量保存
        List<Integer> saveList = Lists.newArrayList(1, 2, 3, 4, 5);
        List<List<Integer>> splitList = ListUtil.split(saveList, 3);
        List<CompletableFuture<Void>> futureList = splitList.stream()
                .map(itemList -> CompletableFuture.runAsync(() ->
                        {
                            // 模拟批量保存逻辑 ...
                            // ThreadUtil.sleep(1, TimeUnit.SECONDS);
                            System.out.println(DateUtil.now() + ": " + JSONUtil.toJsonStr(itemList));
                        },
                        executorService))
                .collect(Collectors.toList());

        // 异步阻塞，等待所有线程执行完
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
    }

}