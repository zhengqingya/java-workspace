package com.zhengqing.demo.config;

import com.github.wujiuye.datasource.sqlwatcher.*;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class TestTableFieldObserver implements TableFieldObserver, InitializingBean {

    @Override
    public WatchMetadata getObserveMetadata() {
        return null;
    }

    @Override
    public Set<WatchMetadata> observeMetadatas() {
        // 在这里注册要监听哪些表的哪些字段
        WatchMetadata watchMetadata = new WatchMetadata();
        watchMetadata.setTable("t_demo");
        watchMetadata.addField("id");
        watchMetadata.addField("username");
        return Sets.newHashSet(watchMetadata);
    }

    /**
     * 监听到sql时被同步调用
     *
     * @param commandType 事件类型
     * @param matchResult 匹配的ITEM
     * @return 返回异步消费者
     */
    @Override
    public AsyncConsumer observe(CommandType commandType, MatchItem matchResult) {
        log.info("commandType:{}, matchResult:{}", commandType, matchResult);
        // 同步消费
        // 这里是sql执行之前，可在sql执行之前做一些事情，比如新旧数据的对比，这里查出旧数据

        // 异步消费，再sql执行完成时，或者在事务方法执行完成时（如果存在事务），完成指：正常执行完成 or 方法异常退出
        return throwable -> {
            // sql执行抛出异常不处理
            if (throwable != null) {
                log.error("异常执行...");
                return;
            }
            // 消费事件
            log.info("正常执行...");
            // ....
        };
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}