package com.zhengqing.demo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p> es测试 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/6/4 20:16
 */
@Slf4j
public class App {

    private static final String ES_INDEX = "user";

    private static RestHighLevelClient getClient() {
        LoggingSystem.get(LoggingSystem.class.getClassLoader()).setLogLevel("root", LogLevel.INFO);
        return new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")).setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(new BasicCredentialsProvider() {{
            setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "123456"));
        }})));
    }

    public static class test_index {
        // 查看 http://localhost:9200/user
        final String MAPPING_TEMPLATE = "{\"mappings\":{\"properties\":{\"age\":{\"type\":\"long\"},\"name\":{\"type\":\"keyword\"},\"content\":{\"type\":\"text\",\"analyzer\":\"ik_max_word\"},\"explain\":{\"type\":\"text\",\"fields\":{\"explain-alias\":{\"type\":\"keyword\"}}},\"sex\":{\"type\":\"keyword\"},\"desc\":{\"type\":\"text\"}}}}";

        @Test
        public void exists() throws Exception {
            boolean isExist = getClient().indices().exists(new GetIndexRequest(ES_INDEX), RequestOptions.DEFAULT);
            System.out.println(isExist);
        }

        @Test
        public void create() throws Exception {
            CreateIndexResponse createIndexResponse = getClient().indices().create(new CreateIndexRequest(ES_INDEX).source(MAPPING_TEMPLATE, XContentType.JSON), RequestOptions.DEFAULT);
            System.out.println(createIndexResponse.isAcknowledged());
        }

        @Test
        public void get() throws Exception {
            GetIndexResponse getIndexResponse = getClient().indices().get(new GetIndexRequest(ES_INDEX), RequestOptions.DEFAULT);
            System.out.println(getIndexResponse.getAliases());
            System.out.println(getIndexResponse.getMappings());
            System.out.println(getIndexResponse.getSettings());
        }

        @Test
        public void delete() throws Exception {
            AcknowledgedResponse acknowledgedResponse = getClient().indices().delete(new DeleteIndexRequest(ES_INDEX), RequestOptions.DEFAULT);
            System.out.println(acknowledgedResponse.isAcknowledged());
        }
    }

    public static class test_document {
        @Test
        public void create() throws Exception {
            RestHighLevelClient client = getClient();
            IndexRequest request = new IndexRequest();
            request.index(ES_INDEX) // 索引
                    .id("1002") // 如果不设置值的情况下，es会默认生成一个_id值
            ;
            request.source(JSONUtil.toJsonStr(User.builder().name(DateUtil.now()).age(RandomUtil.randomInt(100)).sex(RandomUtil.randomString("男女", 1)).build()), XContentType.JSON);
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            System.out.println(JSONUtil.toJsonStr(response));
        }

        @Test
        public void update() throws Exception {
            // tips:id值不存在的情况下去更新数据会报错...
            UpdateRequest request = new UpdateRequest();
            request.index(ES_INDEX).id("1");
            request.doc(JSONUtil.toJsonStr(User.builder().name(DateUtil.now()).age(58).build()), XContentType.JSON);
            // 设置刷新策略 -- 立即刷新
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            UpdateResponse response = getClient().update(request, RequestOptions.DEFAULT);
            System.out.println(response);
        }

        @Test
        public void test_add_or_update() throws Exception {
            // id值存在的时候更新，否则新增
            IndexResponse indexResponse = getClient().index(
                    new IndexRequest(ES_INDEX)
                            .id("111")
                            .opType(DocWriteRequest.OpType.INDEX)
                            .source(JSONUtil.toJsonStr(User.builder().name("AAA1").build()), XContentType.JSON),
                    RequestOptions.DEFAULT);
            System.out.println(indexResponse);
        }

        @Test
        public void get() throws Exception {
            GetRequest request = new GetRequest().index(ES_INDEX).id("1");
            GetResponse response = getClient().get(request, RequestOptions.DEFAULT);
            System.out.println(response);
        }

        @Test
        public void delete() throws Exception {
            DeleteRequest request = new DeleteRequest().index(ES_INDEX).id("1");
            // 设置刷新策略 -- 立即刷新
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            DeleteResponse response = getClient().delete(request, RequestOptions.DEFAULT);
            System.out.println(response);
        }

        @Test
        public void deleteByQuery() throws Exception {
            DeleteByQueryRequest request = new DeleteByQueryRequest(ES_INDEX);
            request.setRefresh(true);  // 确保立即刷新索引
            /**
             * setConflicts 方法用于处理在执行删除操作时可能遇到的版本冲突问题。Elasticsearch 使用乐观锁机制来管理文档版本，以确保并发操作的一致性。当多个操作同时尝试修改同一个文档时，可能会出现版本冲突。
             * setConflicts 方法有以下几个可选值：
             * proceed：
             *      含义：如果遇到冲突，则继续执行删除操作。
             *      效果：即使某些文档由于版本冲突而无法删除，整个删除操作仍然会继续执行其他符合条件的文档。
             *      适用场景：当你希望尽可能多地删除符合条件的文档时使用。
             * abort：
             *      含义：如果遇到任何冲突，则终止整个删除操作。
             *      效果：只要有一个文档因为版本冲突而无法删除，整个删除操作就会失败。
             *      适用场景：当你希望确保所有符合条件的文档都被成功删除时使用。
             * ignore：
             *      含义：如果遇到冲突，则忽略这些冲突。
             *      效果：即使某些文档由于版本冲突而无法删除，也不会抛出异常。
             *      适用场景：当你不关心是否所有符合条件的文档都被成功删除时使用。
             */
            request.setConflicts("proceed");
            request.setQuery(
                    QueryBuilders.rangeQuery("age").gte(50).lte(100)
            );
            BulkByScrollResponse response = getClient().deleteByQuery(request, RequestOptions.DEFAULT);
            System.out.println(response);
        }

        @Test
        public void submitDeleteByQueryTask() throws Exception {
            DeleteByQueryRequest request = new DeleteByQueryRequest(ES_INDEX);
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            boolQuery
//                    .filter(QueryBuilders.termQuery("id", 1))
                    .filter(QueryBuilders.termsQuery("id", Lists.newArrayList(1, 2, 3, 7, 9))).filter(QueryBuilders.termQuery("tenantId", 1));
            request.setQuery(boolQuery);
            // 提交异步删除任务
            getClient().submitDeleteByQueryTask(request, RequestOptions.DEFAULT);
        }
    }


    public static class test_batch {
        @Test
        public void add() throws Exception {
            BulkRequest request = new BulkRequest();
            for (int i = 0; i < 100; i++) {
                String id = String.valueOf(i + 1);
                request.add(new IndexRequest().index(ES_INDEX).id(id).source(JSONUtil.toJsonStr(User.builder().id(Long.valueOf(id)).name(RandomUtil.randomString("张三李四知之愈明，则行之愈笃;行之愈笃，则知之益明。！0123456789", 8)).age(RandomUtil.randomInt(10)).sex(RandomUtil.randomString("男女", 1)).content(DateUtil.now() + RandomUtil.randomString("你一定要努力学习，加油！", 5)).explain(RandomUtil.randomString("奋斗吧少年，你会是最棒的仔！0123456789", 10)).desc(RandomUtil.randomString("奋斗吧少年，你会是最棒的仔！0123456789", 10)).build()), XContentType.JSON));
            }
            BulkResponse response = getClient().bulk(request, RequestOptions.DEFAULT);
            System.out.println(JSONUtil.toJsonStr(response));
        }

        @Test
        public void delete() throws Exception {
            BulkRequest request = new BulkRequest();
            for (int i = 0; i < 10; i++) {
                request.add(new DeleteRequest().index(ES_INDEX).id(String.valueOf(i + 1)));
            }
            BulkResponse res = getClient().bulk(request, RequestOptions.DEFAULT);
            if (res.hasFailures()) {
                log.warn("批量删除数据失败: {}", res.buildFailureMessage());
            }
        }

        @Test
        public void update_method_01() throws Exception {
            // 批量更新单个字段值
            BulkRequest request = new BulkRequest();
            for (int i = 1; i < 3; i++) {
                request.add(new UpdateRequest()
                        .index(ES_INDEX)
                        .id(String.valueOf(i + 3))
                        .doc(JSONUtil.toJsonStr(
                                User.builder().name("@123").build()
                        ), XContentType.JSON)
                );
            }
            BulkResponse res = getClient().bulk(request, RequestOptions.DEFAULT);
            if (res.hasFailures()) {
                log.warn("批量更新部分数据失败: {}", res.buildFailureMessage());
                for (BulkItemResponse resItem : res.getItems()) {
                    if (resItem.isFailed()) {
                        log.warn("操作失败，ID: {}, 原因: {}", resItem.getId(), resItem.getFailureMessage());
                    } else {
                        log.info("操作成功，ID: {}", resItem.getId());
                    }
                }
            }
            System.out.println(res);
        }

        @Test
        public void update_method_02() throws Exception {
            // 根据条件批量更新单个字段值
            UpdateByQueryRequest request = new UpdateByQueryRequest(ES_INDEX);

            // 设置更新值
            request.setScript(new Script(ScriptType.INLINE,
                    "painless",
                    "ctx._source.name = params.newValue", // name改为自己要更新的字段
                    Collections.singletonMap("newValue", "test_value_xxxxxx"))); // 替换为需要设置的值

            // 添加查询条件
            request.setQuery(QueryBuilders.termQuery("age", 18));

            // 执行更新操作
            BulkByScrollResponse response = getClient().updateByQuery(request, RequestOptions.DEFAULT);

            // 检查响应
            long processed = response.getTotal();
            long updated = response.getUpdated();
            System.out.printf("处理了%d条记录，其中更新了%d条记录。%n", processed, updated);
        }

    }

    public static class test_advanced {
        @Test // 条件查询
        public void test() throws Exception {
            // 创建搜索请求对象
            SearchRequest request = new SearchRequest().indices(ES_INDEX);
            // 构建查询的请求体
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // -----------------------------------------------

            // 全量查询
//            sourceBuilder.query(QueryBuilders.matchAllQuery());

            // 完全匹配 -- =
//            sourceBuilder.query(QueryBuilders.termQuery("age", "68"));

            // 范围查询
//            sourceBuilder.query(
//                    QueryBuilders
//                            .rangeQuery("age")
//                            .gte("30")    // 大于等于
//                            .lte("60")  // 小于等于
//            );

            // 组合查询
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                    /**
                     * must——所有的语句都 必须（must） 匹配，与 AND 等价。
                     * must_not——所有的语句都 不能（must not） 匹配，与 NOT 等价。
                     * should——至少有一个语句要匹配，与 OR 等价。
                     * filter——必须匹配，运行在非评分&过滤模式。
                     */
                    // filter 过滤 不会计算分值，性能比must高；must会计算分值
//                    .filter(QueryBuilders.termQuery("age", 2)) // termQuery 精准匹配
                    /**
                     * termQuery 精准匹配 text字段类型 时 需要加上`.别名字段`才能查询出 （tips：同时需要建立索引时对该字段通过fields进行多字段配置）
                     * 同字段多type配置
                     * PUT my-index-000001
                     * {
                     *   "mappings": {
                     *     "properties": {
                     *       "explain": {
                     *         "type": "text",
                     *         "fields": {
                     *           "explain-alias": {
                     *             "type":  "keyword"
                     *           }
                     *         }
                     *       }
                     *     }
                     *   }
                     * }
                     */
//                    .filter(QueryBuilders.termQuery("explain.explain-alias", "的斗年仔斗"))
//                    .filter(QueryBuilders.matchQuery("explain", "斗年"))
//                    .must(QueryBuilders.matchQuery("content", "努力")) // 模糊查询（text字段类型才行） -- 分词后倒排索引查询结果更多
//                    .filter(QueryBuilders.matchPhraseQuery("desc", "12")) // 确保搜索词条在文档中的顺序与查询中的顺序一致
                    .must(like("desc", "学习")) // 模糊分词查询 -- text字段
//                    .must(QueryBuilders.wildcardQuery("name", "*三三*")) // 模糊查询，类似mysql like -- 需keyword字段类型
//                    .must(QueryBuilders.matchPhraseQuery("name", "三三"))
//                    .must(QueryBuilders.matchQuery("age", "68")) // must -- and
//                    .mustNot(QueryBuilders.matchQuery("name", "xxx"))  // mustNot -- 排除 !=
//                    .should(QueryBuilders.matchQuery("sex", "男"))  // should -- or
//                    .filter(QueryBuilders.termsQuery("id", Lists.newArrayList(1,2,3,7,9))) // termsQuery -- in
                    ;
            sourceBuilder.query(boolQueryBuilder);


            // 高亮查询
//            sourceBuilder.query(QueryBuilders.matchQuery("name", "努力"));
//            // 构建高亮字段
//            HighlightBuilder highlightBuilder = new HighlightBuilder()
//                    .preTags("<em color='red'>")//设置标签前缀
//                    .postTags("</em>")//设置标签后缀
//                    .field("name");//设置高亮字段
//            // 设置高亮构建对象
//            sourceBuilder.highlighter(highlightBuilder);

            // 最大值查询
//            sourceBuilder.aggregation(AggregationBuilders.max("maxAge").field("age"));

            // 分组 -- Aggregations 中 docCount 记录了分组后的数量
//            sourceBuilder.aggregation(AggregationBuilders.terms("age_groupby").field("age"));

            // 排序 -- 升序
//            sourceBuilder.sort("age", SortOrder.DESC);

            // 分页查询
            sourceBuilder.from(0).size(10);
            // 默认限制最大10000，超出报错： from + size must be less than or equal to: [10000] but was [10003].
            // trackTotalHits 用于精确统计总数，即获取真实总数量  -- 当超出10000数据量时，`hits.total.value`将不会增长，固定为 10000
//            sourceBuilder.trackTotalHits(true).from(10000).size(3);


            // -----------------------------------------------

            request.source(sourceBuilder);
            SearchResponse response = getClient().search(request, RequestOptions.DEFAULT);
            // 查询匹配
            SearchHits hits = response.getHits();
            System.out.println("took:" + response.getTook());
            System.out.println("timeout:" + response.isTimedOut());
            System.out.println("total:" + hits.getTotalHits());
            System.out.println("MaxScore:" + hits.getMaxScore());
            System.out.println("Aggregations:" + JSONUtil.toJsonStr(response.getAggregations()));

            System.out.println("hits========>>");
            for (SearchHit hit : hits) {
                // 输出每条查询的结果信息
                System.out.println(hit.getSourceAsString());

                // 打印高亮结果
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                if (MapUtil.isNotEmpty(highlightFields)) {
                    System.err.println(highlightFields);
                }
            }
            System.out.println("<<========");
        }

        /**
         * 分词 或 模糊匹配 boolQuery
         *
         * @param fieldName 字段名
         * @param value     查询值
         * @return 组合查询
         * @author zhengqingya
         * @date 2024/6/6 21:15
         */
        private BoolQueryBuilder like(String fieldName, Object value) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            if (isAllChinese(String.valueOf(value))) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(fieldName, value));
            } else {
                // 中文匹配
                boolQueryBuilder.should(QueryBuilders.matchPhraseQuery(fieldName, value));
                // 英文模糊匹配
                boolQueryBuilder.should(QueryBuilders.wildcardQuery(fieldName, "*" + value + "*"));
            }
            return boolQueryBuilder;
        }

        // 判断字符串是否为全中文
        private boolean isAllChinese(String str) {
            String regex = "[\\u4e00-\\u9fa5]+";
            return str.matches(regex);
        }

        @Test
        public void test_from_size() throws Exception {
            SearchRequest request = new SearchRequest().indices(ES_INDEX);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder
//                    .query(QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("name", "张张")))
                    .sort("id", SortOrder.ASC)
                    .from(50)
                    .size(3).trackTotalHits(true);

            request.source(sourceBuilder);
            SearchResponse response = getClient().search(request, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            System.out.println("took:" + response.getTook());
            System.out.println("timeout:" + response.isTimedOut());
            System.out.println("total:" + hits.getTotalHits());
            System.out.println("MaxScore:" + hits.getMaxScore());
            System.out.println("Aggregations:" + JSONUtil.toJsonStr(response.getAggregations()));
            for (SearchHit hit : hits) {
                System.out.println(hit.getSourceAsString());
            }
        }

        @Test
        public void test_search_after_01() throws Exception {
            SearchRequest request = new SearchRequest().indices(ES_INDEX);

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder
//                    .query(QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("name", "张张")))
                    .sort("id", SortOrder.ASC).from(0).size(3).trackTotalHits(true);

            request.source(sourceBuilder);
            SearchResponse response = getClient().search(request, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            for (SearchHit hit : hits) {
                System.out.println(hit.getSourceAsString());
            }

            if (hits.getHits().length == 0) {
                return;
            }
            // 拿到最后一条数据的排序值
            Object[] sortValues = hits.getAt(hits.getHits().length - 1).getSortValues();
            System.out.println("最后一条数据排序值：" + Arrays.toString(sortValues)); // new Object[]{3}

            // 下一页 设置上一页拿到的排序值
            request.source().searchAfter(sortValues);
            response = getClient().search(request, RequestOptions.DEFAULT);
            for (SearchHit hit : response.getHits()) {
                System.out.println("下一页：" + hit.getSourceAsString());
            }
        }

        @Test // 根据上一页排序值直接查询下一页数据
        public void test_search_after_02() throws Exception {
            SearchRequest request = new SearchRequest().indices(ES_INDEX);

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder
//                    .query(QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("name", "张张")))
                    .sort("id", SortOrder.ASC).from(0) // 有searchAfter 时 from值只能为0
                    .size(3).trackTotalHits(true).searchAfter(new Object[]{3}); // 第一页数据中拿到的最后一条数据的排序值 -- hits.getAt(hits.getHits().length - 1).getSortValues();

            request.source(sourceBuilder);
            SearchResponse response = getClient().search(request, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            System.out.println("took:" + response.getTook());
            System.out.println("timeout:" + response.isTimedOut());
            System.out.println("total:" + hits.getTotalHits());
            System.out.println("MaxScore:" + hits.getMaxScore());
            System.out.println("Aggregations:" + JSONUtil.toJsonStr(response.getAggregations()));
            for (SearchHit hit : hits) {
                System.out.println(hit.getSourceAsString());
            }
        }

        @Test
        public void test_scroll() throws Exception {
            // 保持5分钟
            Scroll scroll = new Scroll(TimeValue.timeValueMinutes(5L));
            SearchRequest request = new SearchRequest().indices(ES_INDEX).scroll(scroll);

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder
//                    .query(QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("name", "张张")))
                    .sort("id", SortOrder.ASC).size(3).trackTotalHits(true);

            request.source(sourceBuilder);
            SearchResponse response = getClient().search(request, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            System.out.println("took:" + response.getTook());
            System.out.println("timeout:" + response.isTimedOut());
            System.out.println("total:" + hits.getTotalHits());
            System.out.println("MaxScore:" + hits.getMaxScore());
            System.out.println("Aggregations:" + JSONUtil.toJsonStr(response.getAggregations()));
            for (SearchHit hit : hits) {
                System.out.println(hit.getSourceAsString());
            }

            List<String> scrollIdList = Lists.newArrayList();

            // 下一页
            String scrollId = response.getScrollId();
            int sum = 1;
            while (CollUtil.isNotEmpty(hits)) {
                sum++;
                if (sum > 3) {
                    break;
                }
                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                scrollRequest.scroll(scroll);
                response = getClient().scroll(scrollRequest, RequestOptions.DEFAULT);
                scrollId = response.getScrollId();
                hits = response.getHits();
                System.out.println("scrollId:" + scrollId);
                for (SearchHit hit : hits) {
                    System.out.println("下一页：" + hit.getSourceAsString());
                }
                scrollIdList.add(scrollId);
            }

            // 清除 Scroll 上下文
            clearScrollId(scrollIdList);
        }

        private void clearScrollId(List<String> scrollIdList) throws IOException {
            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
            clearScrollRequest.setScrollIds(scrollIdList);
            getClient().clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
        }

    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {

        private Long id;
        private String name;
        private Integer age;
        private String sex;
        private String content;
        private String explain;
        private String desc;

    }

}
