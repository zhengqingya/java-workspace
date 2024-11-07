//package com.zhengqing.demo;
//
//import com.zhengqing.demo.service.UserService;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.core.env.Environment;
//
//@Slf4j
//public class _02_test_mock_value_fail {
//    @InjectMocks
//    private UserService userService;
//    @MockBean
//    private Environment environment;
//
//    @Before
//    public void init() {
//        MockitoAnnotations.openMocks(this);
//
//        // 这种方式不行，失败...
//        Mockito.when(environment.getProperty("config.limit-num", Integer.class)).thenReturn(30);
//    }
//
//    // ==================== ↓↓↓↓↓↓ 单测case ↓↓↓↓↓↓=======================
//
//    @Test
//    public void test() throws Exception {
//        userService._02_test_spring_value();
//    }
//
//}
