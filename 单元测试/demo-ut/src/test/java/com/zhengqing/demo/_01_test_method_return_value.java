package com.zhengqing.demo;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.zhengqing.demo.entity.User;
import com.zhengqing.demo.mapper.UserMapper;
import com.zhengqing.demo.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.Assert.assertThrows;


public class _01_test_method_return_value {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_mock_value() {
        // 创建一个模拟的用户对象
        User user = User.builder().id(1).username("zhengqingya").build();

        // 模拟 userMapper.selectById 方法的返回值
        Mockito.when(userMapper.selectById(1)).thenReturn(user);

        // 调用被测试的方法
        User result = userService.getUserById(1);

        // 验证结果
        Assert.assertEquals(user, result);
    }

    @Test
    public void test_mock_multi_value() {
        Mockito.when(userMapper.selectById(Mockito.anyInt())).thenReturn(
                User.builder().id(1).username("zq").build(),
                User.builder().id(2).username("zs").build()
        );

        System.out.println(JSONUtil.toJsonStr(userService.getUserById(1)));
        System.out.println(JSONUtil.toJsonStr(userService.getUserById(1)));
    }

    @Test
    public void test_mock_multi_params() {
        Mockito.when(userMapper.selectNameById(Mockito.anyInt())).thenReturn("其它");
        Mockito.when(userMapper.selectNameById(ArgumentMatchers.intThat(e -> e == 1))).thenReturn("zs");
        Mockito.when(userMapper.selectNameById(ArgumentMatchers.intThat(e -> e > 3))).thenReturn("zq");
        Mockito.when(userMapper.selectNameById(ArgumentMatchers.eq(6))).thenReturn("66");

        System.out.println(userService.getNameById(0));
        System.out.println(userService.getNameById(1));
        System.out.println(userService.getNameById(5));
        System.out.println(userService.getNameById(6));
    }

    @Test
    public void test_mock_dynamic_return_value() {
        Mockito.when(userMapper.selectNameById(Mockito.anyInt()))
                .thenAnswer(e -> {
                    // 拿到第一个参数值 -- 根据参数动态响应
                    Integer arg1 = e.getArgument(0, Integer.class);
                    if (arg1 == 1) {
                        return "111";
                    }
                    return RandomUtil.randomString(5);
                });

        System.out.println(userService.getNameById(1));
        System.out.println(userService.getNameById(2));
        System.out.println(userService.getNameById(2));
    }

    @Test
    public void test_throw_error_return_value() {
        Mockito.when(userMapper.selectById(Mockito.any()))
                .thenThrow(
                        new RuntimeException("User not found ...")
                );
        assertThrows(RuntimeException.class, () -> userService.getUserById(1));
    }

    @Test
    public void test_throw_error_no_return_value() {
        Mockito.doThrow(new RuntimeException("error ..."))
                .when(userMapper).executeNoReturnValue(Mockito.any());
        assertThrows(RuntimeException.class, () -> userService._01_test_no_return_value());
    }

    @Test
    public void test_throw_error_action_num() {
        // 第一次调用时 doNothing，第二次调用时抛出异常
        Mockito.doNothing()
                .doThrow(new RuntimeException("error ..."))
                .when(userMapper).executeNoReturnValue(Mockito.any());

        userService._01_test_no_return_value();
        assertThrows(RuntimeException.class, () -> userService._01_test_no_return_value());

        // 验证方法调用次数
        Mockito.verify(userMapper, Mockito.times(2)).executeNoReturnValue(Mockito.any());
    }

    @Test
    public void test_no_return_value() {
        Mockito.doNothing().when(userMapper).executeNoReturnValue(Mockito.anyInt());
        userService._01_test_no_return_value();
    }
}