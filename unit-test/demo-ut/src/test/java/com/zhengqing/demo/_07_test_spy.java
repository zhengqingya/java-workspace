package com.zhengqing.demo;

import com.zhengqing.demo.entity.User;
import com.zhengqing.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doNothing;

@Slf4j
public class _07_test_spy {
    @InjectMocks
    private UserService userService;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    // ==================== ↓↓↓↓↓↓ 单测case ↓↓↓↓↓↓=======================

    @Test
    public void test_spy() throws Exception {
        User spyUser = Mockito.spy(new User()); // 假设存在无参构造函数
        doNothing().when(spyUser).doBusiness(11);

        spyUser.doBusiness(11); // 不会执行内部方法逻辑

        User user = new User();
        user.doBusiness(11); // 会执行内部方法逻辑
    }

}
