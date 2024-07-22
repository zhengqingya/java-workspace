package com.zhengqing.demo.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.CheckedFunction;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * <p> ES方法执行拦截器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/7/22 23:12
 */
@Slf4j
@Aspect
@Component
public class EsAop {

    /**
     * 问题：
     * {@link RestHighLevelClient#internalPerformRequest(Object, CheckedFunction, RequestOptions, CheckedFunction, Set)}
     * 中的 client.performRequest(req); 第二次执行delete时为null
     */

    @SneakyThrows
    @Around("execution(* org.elasticsearch.client.RestHighLevelClient.search(..))")
//    @Around("execution(* com.zhengqing.demo.api.*Controller.*(..))")
    public Object addTenantIdToSearch(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        if (args[0] instanceof SearchRequest) {
            SearchRequest request = (SearchRequest) args[0];

            // 在这里设置你的tenantId，你可以从上下文、请求头或其他地方获取
            String tenantId = "your-tenant-id";
            request.source(request.source().query(QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("tenantId", tenantId))));
        }
        return pjp.proceed();
    }

}
