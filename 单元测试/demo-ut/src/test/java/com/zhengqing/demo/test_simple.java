package com.zhengqing.demo;

import com.zhengqing.demo.entity.User;
import com.zhengqing.demo.mapper.UserMapper;
import com.zhengqing.demo.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class test_simple {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test() {
        // 创建一个模拟的用户对象
        User user = User.builder().id(1).username("zhengqingya").build();

        // 模拟 userMapper.selectById 方法的返回值
        Mockito.when(userMapper.selectById(1)).thenReturn(user);

        // 调用被测试的方法
        User result = userService.getUserById(1);

        // 验证结果
        Assert.assertEquals(user, result);
    }

}