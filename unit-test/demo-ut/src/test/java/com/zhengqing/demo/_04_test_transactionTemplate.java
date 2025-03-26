package com.zhengqing.demo;

import com.zhengqing.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Consumer;

@Slf4j
public class _04_test_transactionTemplate {
    @InjectMocks
    private UserService userService;

    @Mock
    private TransactionTemplate transactionTemplate;


    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    // ==================== ↓↓↓↓↓↓ 单测case ↓↓↓↓↓↓=======================

    @Test
    public void test_mock_transactionTemplate() throws Exception {
        Mockito.doAnswer(invocation -> {
            Consumer<TransactionStatus> callback = invocation.getArgument(0);
            TransactionStatus transactionStatus = Mockito.mock(TransactionStatus.class);
            callback.accept(transactionStatus);
            return null;
        }).when(transactionTemplate).executeWithoutResult(ArgumentMatchers.any(Consumer.class));

        userService._04_test_transactionTemplate();
    }

}
