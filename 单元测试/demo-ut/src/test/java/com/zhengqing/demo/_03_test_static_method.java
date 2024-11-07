package com.zhengqing.demo;

import com.zhengqing.demo.service.UserService;
import com.zhengqing.demo.util.TestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@Slf4j
public class _03_test_static_method {
    @InjectMocks
    private UserService userService;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
//        Mockito.mockStatic(TestUtil.class);
    }

    // ==================== ↓↓↓↓↓↓ 单测case ↓↓↓↓↓↓=======================

    @Test
    public void test_mock_static_method() throws Exception {
        Mockito.mockStatic(TestUtil.class); // 不需要返回值的话，这个就可以模拟静态方法执行了...
        // 模拟静态方法返回值
        Mockito.when(TestUtil.getNowTime(Mockito.anyInt())).thenReturn("xxx");
        userService._03_test_static_method();
    }

    @Test
    public void test_mock_static_method_2() throws Exception {
        // 创建静态方法的模拟对象
        try (MockedStatic<TestUtil> theMock = Mockito.mockStatic(TestUtil.class)) {
            // 指定静态方法的返回值
            theMock.when(() -> TestUtil.getNowTime(Mockito.anyInt())).thenReturn("xxx");
            userService._03_test_static_method();
        }
    }

}
