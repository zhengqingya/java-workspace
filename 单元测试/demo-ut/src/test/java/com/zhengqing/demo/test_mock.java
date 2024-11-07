package com.zhengqing.demo;

import com.zhengqing.demo.service.UserService;
import com.zhengqing.demo.util.TestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * <p> mockito </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/9/25 14:43
 */
@Slf4j
public class test_mock {
    @InjectMocks
    private UserService userService;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        Mockito.mockStatic(TestUtil.class);
    }

    // ==================== ↓↓↓↓↓↓ 单测case ↓↓↓↓↓↓=======================


    // 同一场景多值单元测试
    @ParameterizedTest
    @ValueSource(strings = {"11", "666"})
    void test_multi_value(String str) {
        System.out.println(Integer.parseInt(str));
    }

    @Test
    public void test() throws Exception {
        System.err.println("test...");
    }


}
