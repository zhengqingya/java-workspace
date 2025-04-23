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
