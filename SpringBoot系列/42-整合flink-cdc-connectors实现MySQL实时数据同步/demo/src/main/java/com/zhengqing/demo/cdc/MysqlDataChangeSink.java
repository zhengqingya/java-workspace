package com.zhengqing.demo.cdc;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.springframework.stereotype.Component;

import java.io.Serializable;


/**
 * <p> MySQL数据改变处理 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/2/22 17:48
 */
@Slf4j
@Component
public class MysqlDataChangeSink implements SinkFunction<MysqlDataBO>, Serializable {

    @Override
    public void invoke(MysqlDataBO value, Context context) throws Exception {
        log.info("监听数据: {}", JSONUtil.toJsonStr(value));
        // 拿到变更数据做业务逻辑处理...
    }

}
