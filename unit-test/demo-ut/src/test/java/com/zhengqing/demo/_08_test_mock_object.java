package com.zhengqing.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doNothing;

@Slf4j
public class _08_test_mock_object {
    @Mock
    private User mockUser;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    // ==================== ↓↓↓↓↓↓ 单测case ↓↓↓↓↓↓=======================

    @Test
    public void test() throws Exception {
        /**
         * 方法1:
         * @Mock
         * private User mockUser;
         */
        mockUser.doBusiness(); // 不会执行内部方法逻辑

        /**
         * 方法2：
         * Mockito.mock(xx.class);
         */
        User user = Mockito.mock(User.class);
        doNothing().when(user).doBusiness();

        user.doBusiness(); // 不会执行内部方法逻辑

        new User().doBusiness(); // 会执行内部方法逻辑
    }

    public static class User {
        public void doBusiness() {
            log.info("doBusiness");
        }
    }

}
