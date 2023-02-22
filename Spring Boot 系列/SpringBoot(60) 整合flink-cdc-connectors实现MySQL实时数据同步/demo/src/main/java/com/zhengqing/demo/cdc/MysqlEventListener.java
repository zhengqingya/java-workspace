package com.zhengqing.demo.cdc;


import cn.hutool.json.JSONObject;
import com.ververica.cdc.connectors.mysql.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.DebeziumDeserializationSchema;
import com.ververica.cdc.debezium.DebeziumSourceFunction;
import io.debezium.data.Envelope;
import lombok.RequiredArgsConstructor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p> MySQL数据监听 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/2/22 17:48
 */
@Component
@RequiredArgsConstructor
public class MysqlEventListener implements ApplicationRunner {
    private final MysqlDataChangeSink mySQLDataChangeSink;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        DebeziumSourceFunction<MysqlDataBO> mySqlSource = MySqlSource.builder()
                .hostname("127.0.0.1")
                .port(3306)
                .databaseList("demo")
                .tableList("demo.t_test")
                .username("root")
                .password("root")
                // 增量数据
                .startupOptions(StartupOptions.latest())
                .deserializer(new DebeziumDeserializationSchema() {
                    @Override
                    public TypeInformation getProducedType() {
                        return TypeInformation.of(MysqlDataBO.class);
                    }

                    @Override
                    public void deserialize(SourceRecord sourceRecord, Collector collector) throws Exception {
                        String topic = sourceRecord.topic();
                        String[] fields = topic.split("\\.");
                        String database = fields[1];
                        String table = fields[2];
                        Envelope.Operation operationType = Envelope.operationFor(sourceRecord);
                        Struct struct = (Struct) sourceRecord.value();
                        String before = this.getJsonStr(struct, "before");
                        String after = this.getJsonStr(struct, "after");
                        collector.collect(MysqlDataBO.builder()
                                .database(database)
                                .table(table)
                                .operationType(operationType.toString())
                                .before(before)
                                .after(after)
                                .build());
                    }

                    private String getJsonStr(Struct struct, String fieldName) {
                        Struct data = struct.getStruct(fieldName);
                        JSONObject result = new JSONObject();
                        if (data != null) {
                            Schema schema = data.schema();
                            List<Field> fieldList = schema.fields();
                            fieldList.forEach(e -> result.put(e.name(), data.get(e)));
                        }
                        return result.toString();
                    }
                })
                .build();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // enable checkpoint
        env.enableCheckpointing(3000);

        DataStream<MysqlDataBO> streamSource = env
                .addSource(mySqlSource, "MySQL Source")
                .setParallelism(1);
        streamSource.addSink(this.mySQLDataChangeSink);

        env.execute("mysql-cdc");
    }

}
