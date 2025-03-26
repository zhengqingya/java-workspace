package com.zhengqing.demo;

import com.zhengqing.demo.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class _05_test_mock_log {
    @InjectMocks
    private LogService logService;
//    @Mock
//    private Logger mockLogger;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    // ==================== ↓↓↓↓↓↓ 单测case ↓↓↓↓↓↓=======================

    @Test
    public void testMockLogIsDebugEnabled() throws Exception {
        /**
         * 编译后的class文件中会生成 private static final Logger log = LoggerFactory.getLogger(LogService.class);
         * 所以我们可以对log进行mock
         */
        // 获取final字段
        Field field = LogService.class.getDeclaredField("log");

        // 设置字段可访问
        field.setAccessible(true);

        // 移除final修饰符
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.set(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        // 修改字段值
        org.slf4j.Logger mockLog = mock(LoggerFactory.getLogger(LogService.class).getClass());
        when(mockLog.isDebugEnabled()).thenReturn(false);
        field.set(null, mockLog);

        logService._mock_log();
    }
}

