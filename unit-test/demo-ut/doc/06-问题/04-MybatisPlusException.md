# MybatisPlusException

场景：

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    
    public void error_04() {
        System.out.println("error_04...");
        String username = "zq";
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(username), User::getUsername, username)
                .orderByDesc(User::getId);
        IPage<User> page = userMapper.selectPage(new Page<>(1, 10), queryWrapper);
        System.out.println(JSONUtil.toJsonStr(page));
    }
}
```

mock问题：

```shell
com.baomidou.mybatisplus.core.exceptions.MybatisPlusException: can not find lambda cache for this entity [com.zhengqing.demo.entity.User]

	at com.baomidou.mybatisplus.core.toolkit.ExceptionUtils.mpe(ExceptionUtils.java:49)
	at com.baomidou.mybatisplus.core.toolkit.Assert.isTrue(Assert.java:38)
	at com.baomidou.mybatisplus.core.toolkit.Assert.notNull(Assert.java:72)
	at com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper.tryInitCache(AbstractLambdaWrapper.java:89)
	at com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper.getColumnCache(AbstractLambdaWrapper.java:78)
	at com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper.columnToString(AbstractLambdaWrapper.java:62)
	at com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper.columnToString(AbstractLambdaWrapper.java:58)
	at com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper.columnToString(AbstractLambdaWrapper.java:38)
```

解决：

```java
@Before
public void init() {
    MockitoAnnotations.openMocks(this);
    TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), User.class);
}
```
