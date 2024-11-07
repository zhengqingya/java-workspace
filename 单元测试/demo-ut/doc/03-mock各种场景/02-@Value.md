# @Value

### 场景

模拟或设置值 `@Value` 注解的字段

```java
package com.zhengqing.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Value("${config.limit-num:3}")
    private Integer limitNum;

    public void _02_test_spring_value() {
        System.out.println("limitNum：" + limitNum);
    }
}
```

### 单测

#### 法一：Spring 提供了 ReflectionTestUtils 类，可以用来在测试中设置私有字段的值。

```java
package com.zhengqing.demo;

import com.zhengqing.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
public class test_mock_value_1 {
    @InjectMocks
    private UserService userService;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);

        // Spring 提供了 ReflectionTestUtils 类，可以用来在测试中设置私有字段的值。
        ReflectionTestUtils.setField(userService, "limitNum", 3);
    }

    // ==================== ↓↓↓↓↓↓ 单测case ↓↓↓↓↓↓=======================

    @Test
    public void test() throws Exception {
        userService._02_test_spring_value();
    }

}
```

#### 法二（失败版）：@MockBean 来创建一个模拟的 Environment 对象，并设置属性值。

> 失败，没有spring上下文环境...，要想有效的话，集成测试环境，等于没说 ^_^

```java

@MockBean
private Environment environment;

@Before
public void init() {
    MockitoAnnotations.openMocks(this);

    // 这种方式不行，失败...
    Mockito.when(environment.getProperty("config.limit-num", Integer.class)).thenReturn(30);
}
```
