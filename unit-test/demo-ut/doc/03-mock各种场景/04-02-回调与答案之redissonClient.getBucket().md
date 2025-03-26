# 回调与答案 之 redissonClient.getBucket()

```
private final RedissonClient redissonClient;
RBucket<Long> bucket = redissonClient.getBucket("key_1");
bucket.set(1L, 1, TimeUnit.HOURS);
```

mock

```
// 这种方式不行!
// RBucket<Long> bucket = Mockito.mock(RBucket.class);
// when(redissonClient.getBucket(anyString())).thenReturn(bucket);


// 解决：明确指定 RBucket<Long> 泛型类型
RBucket<Long> bucket = Mockito.mock(RBucket.class);
when(redissonClient.getBucket(Mockito.anyString())).thenAnswer(invocation -> {
    String key = invocation.getArgument(0, String.class);
    return bucket; // 返回明确类型的 mock 对象
});


// 或
when(redissonClient.getBucket(Mockito.anyString())).thenAnswer(invocation -> Mockito.mock(RBucket.class));
```
