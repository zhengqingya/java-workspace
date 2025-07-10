package com.zhengqing.demo;

import com.zhengqing.demo.entity.User;
import com.zhengqing.demo.mapper.UserMapper;
import com.zhengqing.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ExecutorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;


public class _100_test_project {

    @Mock
    private ExecutorService executorService;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_100_CompletableFuture() {
        /**
         * 使用 Mockito 模拟 ExecutorService 的 execute 方法，使其在接收到 Runnable 任务时立即同步执行该任务，而不是异步提交给线程池。
         * 这样做的目的是为了在单元测试中确保异步逻辑能被触发并可验证结果。
         */
        Mockito.doAnswer(
                (invocation) -> {
                    ((Runnable) invocation.getArguments()[0]).run();
                    return null;
                }
        ).when(executorService).execute(any(Runnable.class));

        userService._100_CompletableFuture();
    }

    @Test
    public void test_100_mybatis_save_after_getId() {
        doAnswer(invocation -> {
            User arg = invocation.getArgument(0);
            arg.setId(666);
            return null;
        }).when(userMapper).insert(any(User.class));

        userService._100_mybatis_save_after_getId();
    }

}