package com.zhengqing.demo;

import com.zhengqing.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
public class _02_test_mock_value {
    @InjectMocks
    private UserService userService;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);

        // Spring 提供了 ReflectionTestUtils 类，可以用来在测试中设置私有字段的值。
        ReflectionTestUtils.setField(userService, "limitNum", 3);
    }

    // ==================== ↓↓↓↓↓↓ 单测case ↓↓↓↓↓↓=======================

    @Test
    public void test() throws Exception {
        userService._02_test_spring_value();
    }

}
