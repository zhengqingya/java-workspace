package com.zhengqing.demo;

import com.zhengqing.demo.mapper.UserMapper;
import com.zhengqing.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Slf4j
public class _06_test_result_verify {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    // ==================== ↓↓↓↓↓↓ 单测case ↓↓↓↓↓↓=======================

    @Test
    public void test_case_1() throws Exception {
        // 创建模拟对象
        List mockedList = mock(List.class);

        // 调用方法
        mockedList.add("one");
        mockedList.add("two");

        // 验证 add 方法被调用了两次
        Mockito.verify(mockedList, Mockito.times(2)).add(Mockito.anyString());
    }

    @Test
    public void test_case_2() throws Exception {
        // 创建模拟对象
        List firstMock = mock(List.class);
        List secondMock = mock(List.class);

        // 调用方法
        firstMock.add("was called first");
        secondMock.add("was called second");

        // 验证调用顺序
        InOrder inOrder = inOrder(firstMock, secondMock);
        inOrder.verify(firstMock).add("was called first");
        inOrder.verify(secondMock).add("was called second");
    }

    @Test
    public void test_case_3() throws Exception {
        // 创建模拟对象
        List mockedList = mock(List.class);

        // 调用方法
        mockedList.add("test");

        // 验证 add 方法被调用时传递了 "test" 参数
        verify(mockedList).add("test");
    }

    @Test
    public void test_case_4() throws Exception {
        // 创建模拟对象
        List mockedList = mock(List.class);

        // 调用方法
        mockedList.add("one");

        // 验证 clear 方法未被调用
        verify(mockedList, never()).clear();
    }

    @Test
    public void test_case_5() throws Exception {
        // 创建模拟对象
        List mockedList = mock(List.class);

        // 调用方法
        mockedList.add("one");

        // 验证 add 方法至少被调用了一次
        verify(mockedList, atLeastOnce()).add(anyString());
    }

    @Test
    public void test_case_6() throws Exception {
        // 创建模拟对象
        List mockedList = mock(List.class);

        // 调用方法
        mockedList.add("one");

        // 验证 add 方法被调用了一次且仅一次
        verify(mockedList, times(1)).add("one");
    }

    @Test
    public void test_case_7() throws Exception {
        // 创建模拟对象
        List mockedList = mock(List.class);

        // 设置模拟方法的返回值
        when(mockedList.size()).thenReturn(10);

        // 调用方法
        int size = mockedList.size();

        // 验证返回值
        assertEquals(10, size);
        AssertionsForClassTypes.assertThat(size).isEqualTo(10);
    }

    @Test
    public void test_case_8() throws Exception {
        // 创建模拟对象
        List mockedList = mock(List.class);

        // 配置 mock 对象，当调用 add 方法时抛出异常
        doThrow(new RuntimeException("xxx")).when(mockedList).add(any());

        //  验证方法调用是否抛出预期的异常
        assertThrows(RuntimeException.class, () -> mockedList.add(1));

        assertThatThrownBy(() -> mockedList.add(1))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("xx")
//                .hasFieldOrPropertyWithValue("errorCode", 400)
        ;
    }
}
