# 项目常用mock案例

### StringRedisTemplate

```
@Mock
private StringRedisTemplate stringRedisTemplate;


ValueOperations<String, String> mockRedisTemplate = mock(ValueOperations.class);
when(stringRedisTemplate.opsForValue()).thenReturn(mockRedisTemplate);
when(mockRedisTemplate.increment(anyString(), anyLong())).thenReturn(1L);
```

### RestHighLevelClient

```
@Mock
private RestHighLevelClient restHighLevelClient;


SearchResponse searchResponse = mock(SearchResponse.class);
when(restHighLevelClient.search(any(SearchRequest.class), any(RequestOptions.class))).thenReturn(searchResponse);
SearchHits searchHits = mock(SearchHits.class);
when(searchResponse.getHits()).thenReturn(searchHits);
TotalHits totalHits = new TotalHits(1L, TotalHits.Relation.EQUAL_TO);
when(searchHits.getTotalHits()).thenReturn(totalHits);
```

### TransactionTemplate

```
private final TransactionTemplate transactionTemplate;

Mockito.doAnswer(invocation -> {
            Consumer<TransactionStatus> callback = invocation.getArgument(0);
            TransactionStatus transactionStatus = Mockito.mock(TransactionStatus.class);
            callback.accept(transactionStatus);
            return null;
        }).when(transactionTemplate).executeWithoutResult(ArgumentMatchers.any(Consumer.class));
```

### redissonClient

```
private final RedissonClient redissonClient;


when(redissonClient.getBucket(Mockito.anyString())).thenAnswer(invocation -> Mockito.mock(RBucket.class));
```
