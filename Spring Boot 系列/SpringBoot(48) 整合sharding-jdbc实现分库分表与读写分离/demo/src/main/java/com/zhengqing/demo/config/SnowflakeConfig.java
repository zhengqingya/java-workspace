package com.zhengqing.demo.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

/**
 * <p> Hutool之雪花算法生成唯一ID配置 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/29 15:52
 */
@Slf4j
@Component
public class SnowflakeConfig {

    /**
     * 终端ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private long workerId ;
    /**
     * 数据中心ID
     */
    private long datacenterId = 1;
    private Snowflake snowflake = IdUtil.createSnowflake(workerId,datacenterId);

    @PostConstruct
    public void init(){
        workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
        log.info("当前机器的workId:{}",workerId);
    }

    public synchronized long snowflakeId(){
        return snowflake.nextId();
    }

    public synchronized long snowflakeId(long workerId,long datacenterId){
        Snowflake snowflake = IdUtil.createSnowflake(workerId, datacenterId);
        return snowflake.nextId();
    }

}

