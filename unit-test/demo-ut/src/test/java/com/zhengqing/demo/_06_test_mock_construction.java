package com.zhengqing.demo;

import com.zhengqing.demo.entity.User;
import com.zhengqing.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedConstruction;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

@Slf4j
public class _06_test_mock_construction {
    @InjectMocks
    private UserService userService;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    // ==================== ↓↓↓↓↓↓ 单测case ↓↓↓↓↓↓=======================

    @Test
    public void test_case_1() throws Exception {
        // 使用 try-with-resources 拦截所有 User 构造方法调用
        try (MockedConstruction<User> mockedConstruction = mockConstruction(
                User.class,
                // 自定义构造方法的返回值（根据参数动态返回）
                (mock, context) -> {
                    List<?> args = context.arguments();
                    System.err.println("构造方法参数：" + args);
                    if (args.size() > 0 && (Integer) args.get(0) > 100) {
                        // 当参数值 >100 时，返回默认 Mock 行为
                        when(mock.getId()).thenReturn(-1);
                    } else {
                        // 其他情况返回默认 Mock 行为
                        when(mock.getId()).thenReturn((Integer) args.get(0));
                    }
                }
        )) {
            // 在作用域内，所有 new User(...) 都会被 Mockito 接管
            User user1 = new User(101);
            System.out.println("mock id: " + user1.getId()); // mock id: -1

            userService._06_test_mock_new_instance();
        }

        // 作用域外，恢复原始构造方法行为
        User realUser = new User(11);
        System.out.println("real id: " + realUser.getId()); // real id: 11
    }

    @Test
    public void test_case_2() throws Exception {
        MockedConstruction<User> mockedConstruction = mockConstruction(User.class, (mock, context) -> {
            List<?> args = context.arguments();
//            System.err.println("构造方法参数：" + args);
            if (args.size() > 0 && (Integer) args.get(0) > 100) {
                // 当参数值 >100 时，返回默认 Mock 行为
                when(mock.getId()).thenReturn(-1);
            } else {
                // 其他情况返回默认 Mock 行为
                when(mock.getId()).thenReturn((Integer) args.get(0));
            }
        });
        System.out.println("mock id: " + new User(11).getId()); // mock id: 11
        System.out.println("mock id: " + new User(101).getId()); // mock id: -1

        userService._06_test_mock_new_instance();
        mockedConstruction.close(); // 在测试方法后关闭 Mock 作用域
    }

}
