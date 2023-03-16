package com.zhengqing.demo.mysql;


import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * <p> MySQL数据监听 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/2/22 17:48
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MysqlEventListener implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        BinaryLogClient client = new BinaryLogClient("127.0.0.1", 3306, "root", "root");
//        EventDeserializer eventDeserializer = new EventDeserializer();
//        eventDeserializer.setCompatibilityMode(
//                EventDeserializer.CompatibilityMode.DATE_AND_TIME_AS_LONG,
//                EventDeserializer.CompatibilityMode.CHAR_AND_BINARY_AS_BYTE_ARRAY
//        );
//        client.setEventDeserializer(eventDeserializer);

        // 数据监听
        client.registerEventListener(event -> {
            EventData data = event.getData();

            if (data instanceof TableMapEventData) {
                TableMapEventData tableMapEventData = (TableMapEventData) data;
                System.err.println("db：" + tableMapEventData.getDatabase());
                System.err.println("tableName：" + tableMapEventData.getTable());
            }

            // 表数据修改
            if (data instanceof UpdateRowsEventData) {
                log.info("update: [{}]", data);
            } else if (data instanceof WriteRowsEventData) {
                log.info("insert: [{}]", data);
            } else if (data instanceof DeleteRowsEventData) {
                log.info("delete: [{}]", data);
            }

        });
        client.connect();
    }

}
