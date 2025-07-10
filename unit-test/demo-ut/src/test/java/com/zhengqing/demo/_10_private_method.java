package com.zhengqing.demo;

import com.zhengqing.demo.entity.User;
import com.zhengqing.demo.mapper.UserMapper;
import com.zhengqing.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@Slf4j
public class _10_private_method {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_10_private_method() {
        doAnswer(invocation -> {
            User arg = invocation.getArgument(0);
            arg.setId(666);
            return null;
        }).when(userMapper).insert(any(User.class));

        User result = ReflectionTestUtils.invokeMethod(userService, "_10_private_method", "zq");

        assertThat(result.getId()).isEqualTo(666);
    }
}
