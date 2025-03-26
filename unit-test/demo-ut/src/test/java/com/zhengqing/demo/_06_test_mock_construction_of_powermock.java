//package com.zhengqing.demo;
//
//import cn.hutool.json.JSONUtil;
//import com.zhengqing.demo.entity.User;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.powermock.api.mockito.PowerMockito;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//@Slf4j
//@RunWith(PowerMockRunner.class)
//public class _06_test_mock_construction_of_powermock {
//    @Test
//    public void test_case_1() throws Exception {
//        User user = User.builder().id(-1).username("zhengqingya").build();
//        // 匹配所有 new User(...) 调用，无论参数是什么
//        PowerMockito.whenNew(User.class)
//                .withAnyArguments()  // 匹配任意参数
//                .thenReturn(user);
//        System.out.println("mock: " + JSONUtil.toJsonStr(new User(1))); // mock: {"id":-1,"username":"zhengqingya"}
//        System.out.println("mock: " + JSONUtil.toJsonStr(new User(100))); // mock: {"id":-1,"username":"zhengqingya"}
//    }
//
//    @Test
//    public void test_case_2() throws Exception {
//        User user = User.builder().id(-1).username("zhengqingya").build();
//        PowerMockito.whenNew(User.class).withArguments(11).thenReturn(user);
//
//        System.out.println("mock: " + JSONUtil.toJsonStr(new User(11))); // mock: {"id":-1,"username":"zhengqingya"}
//    }
//}
