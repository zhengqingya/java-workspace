# 私有方法

mock: `ReflectionTestUtils.invokeMethod(对象, 私有方法名, 参数列表);`

### 场景

```java
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private void _10_private_method(String str) {
        User user = User.builder().username("zq").build();
        userMapper.insert(user);
        System.out.println(str + ": " + user.getId().toString());
    }
}
```

### 单测

```java
import org.springframework.test.util.ReflectionTestUtils;

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
    public void test_100_private_method() {
        doAnswer(invocation -> {
            User arg = invocation.getArgument(0);
            arg.setId(666);
            return null;
        }).when(userMapper).insert(any(User.class));

        ReflectionTestUtils.invokeMethod(userService, "_10_private_method", "zq");
    }
}
```