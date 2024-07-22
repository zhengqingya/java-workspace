package com.zhengqing.demo.api;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/restHighLevelClient")
@RequiredArgsConstructor
@Api(tags = "RestHighLevelClient")
public class RestHighLevelClientController {

    private final RestHighLevelClient restHighLevelClient;

    @SneakyThrows
    @GetMapping("search") // http://127.0.0.1/restHighLevelClient/search?name=xx
    public Object search(@RequestParam String name) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("name", name));
        SearchRequest request = new SearchRequest().indices("user").source(sourceBuilder);
        SearchResponse res = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        for (SearchHit hit : res.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
        return res;
    }

}
