# 项目常用mock案例

### 1、StringRedisTemplate

```
private final StringRedisTemplate stringRedisTemplate;

stringRedisTemplate.opsForValue().increment(key, 1);
```

```
@Mock
private StringRedisTemplate stringRedisTemplate;


ValueOperations<String, String> mockRedisTemplate = mock(ValueOperations.class);
when(stringRedisTemplate.opsForValue()).thenReturn(mockRedisTemplate);
when(mockRedisTemplate.increment(anyString(), anyLong())).thenReturn(1L);
```

### 2、RestHighLevelClient

```
private final RestHighLevelClient restHighLevelClient;

SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
SearchHit[] hits = search.getHits().getHits();
if (hits != null && hits.length > 0) {
    List<Long> userIdList = Arrays.stream(hits).map(it -> {
        Map<String, Object> sourceAsMap = it.getSourceAsMap();
        String id = (String) sourceAsMap.get("userId");
        return Long.valueOf(id);
    }).collect(Collectors.toList());
}
```

```
@Mock
private RestHighLevelClient restHighLevelClient;


// -------------------- es ------------------------
// mock 有数据
SearchResponse searchResponse = mock(SearchResponse.class);
SearchHits searchHits = mock(SearchHits.class);
when(searchResponse.getHits()).thenReturn(searchHits);
TotalHits totalHits = new TotalHits(0L, TotalHits.Relation.EQUAL_TO);
when(searchHits.getTotalHits()).thenReturn(totalHits);

SearchHit[] hitsArray = new SearchHit[1];
hitsArray[0] = mock(SearchHit.class);
when(searchHits.getHits()).thenReturn(hitsArray);

Object[] expectedSortValues = new Object[]{100000L, "sort_id"};
when(searchHits.getAt(anyInt())).thenReturn(hitsArray[0]);
when(hitsArray[0].getSortValues()).thenReturn(expectedSortValues);
Map<String, Object> sourceAsMap = Maps.newHashMap();
sourceAsMap.put("taskId", 1L);
sourceAsMap.put("checkType", "THIRD");
sourceAsMap.put("result", "PASS");
when(hitsArray[0].getSourceAsMap()).thenReturn(sourceAsMap);

// mock 无数据
SearchResponse searchResponseOfNoData = mock(SearchResponse.class);
SearchHits searchHitsOfNoData = mock(SearchHits.class);
when(searchResponseOfNoData.getHits()).thenReturn(searchHitsOfNoData);
TotalHits totalHitsOfNoData = new TotalHits(0L, TotalHits.Relation.EQUAL_TO);
when(searchHitsOfNoData.getTotalHits()).thenReturn(totalHitsOfNoData);
when(searchHitsOfNoData.getHits()).thenReturn(new SearchHit[0]);

// 第1次调用有数据，第2次调用无数据 -- 适用于循环拉取数据场景
when(restHighLevelClient.search(any(SearchRequest.class), any(RequestOptions.class))).thenReturn(
    searchResponse, searchResponseOfNoData
);
```

### 3、TransactionTemplate

```
private final TransactionTemplate transactionTemplate;

transactionTemplate.executeWithoutResult(status -> {
    // ...
});
```

```
Mockito.doAnswer(invocation -> {
    Consumer<TransactionStatus> callback = invocation.getArgument(0);
    TransactionStatus transactionStatus = Mockito.mock(TransactionStatus.class);
    callback.accept(transactionStatus);
    return null;
}).when(transactionTemplate).executeWithoutResult(ArgumentMatchers.any(Consumer.class));
```

### 4、redissonClient

```
private final RedissonClient redissonClient;

redissonClient.getBucket("test").trySet("test", 4L, TimeUnit.MINUTES)
```

```
// mock
RBucket rBucket = Mockito.mock(RBucket.class);
when(rBucket.trySet(anyString(), anyLong(), any(TimeUnit.class))).thenReturn(true);
when(redissonClient.getBucket(Mockito.anyString())).thenAnswer(invocation -> rBucket);
```

```
RLock lock = redissonClient.getLock("sys_key");
if (!lock.tryLock()) {
    throw new BizException("请稍后重试！");
}


// mock
RLock rLock = Mockito.mock(RLock.class);
when(redissonClient.getLock(anyString())).thenReturn(rLock);
when(rLock.tryLock()).thenReturn(true);
```

### 5、CompletableFuture

```
public void _100_CompletableFuture() {
    // ExecutorService executorService = Executors.newFixedThreadPool(5); // 这种线程池mock正常运行
    // private final ExecutorService executorService; 这种注入的方式mock会一直转圈儿...

    // 批量保存
    List<Integer> saveList = Lists.newArrayList(1, 2, 3, 4, 5);
    List<List<Integer>> splitList = ListUtil.split(saveList, 3);
    List<CompletableFuture<Void>> futureList = splitList.stream()
            .map(itemList -> CompletableFuture.runAsync(() ->
                    {
                        // 模拟批量保存逻辑 ...
                        // ThreadUtil.sleep(1, TimeUnit.SECONDS);
                        System.out.println(DateUtil.now() + ": " + JSONUtil.toJsonStr(itemList));
                    },
                    executorService))
            .collect(Collectors.toList());

    // 异步阻塞，等待所有线程执行完
    CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
}
```

```
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
```

### 6、LambdaQueryWrapper

```
LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
queryWrapper.like(StrUtil.isNotBlank(username), User::getUsername, username)
            .orderByDesc(User::getId);
```

```java
@Before
public void init() {
    MockitoAnnotations.openMocks(this);
    TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), User.class);
}
```

### 7、xxMapper.insert(xx) 之后生成主键id

```
private final UserMapper userMapper;
userMapper.insert(user);


// mock
doAnswer(invocation -> {
    User arg = invocation.getArgument(0);
    arg.setId(666);
    return null;
}).when(userMapper).insert(any(User.class));
```