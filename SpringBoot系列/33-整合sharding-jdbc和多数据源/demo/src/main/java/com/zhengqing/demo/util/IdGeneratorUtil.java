package com.zhengqing.demo.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * <p> Hutool之雪花算法生成唯一ID配置 </p>
 *
 * @author zhengqingya
 * @description 可参考 https://www.bookstack.cn/read/hutool/bfd2d43bcada297e.md
 * @date 2021/10/29 16:57
 */
@Slf4j
@Component
public class IdGeneratorUtil {

    /**
     * 终端ID
     */
    private long workerId = 0;
    /**
     * 数据中心ID
     */
    private long datacenterId = 1;

    private Snowflake snowflake = IdUtil.createSnowflake(workerId, datacenterId);

    @PostConstruct
    public void init() {
        try {
            workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
            log.info("当前机器的IP:[{}], workerId:[{}]", NetUtil.getLocalhostStr(), workerId);
        } catch (Exception e) {
            log.error("获取当前机器workerId 异常", e);
            workerId = NetUtil.getLocalhostStr().hashCode();
        }
    }

    /**
     * 使用默认的 workerId 和 datacenterId
     */
    public synchronized long snowflakeId() {
        return snowflake.nextId();
    }

    /**
     * 使用自定义的 workerId 和 datacenterId
     */
    public synchronized long snowflakeId(long workerId, long datacenterId) {
        return IdUtil.createSnowflake(workerId, datacenterId).nextId();
    }

    public static void main(String[] args) {
        IdGeneratorUtil idGeneratorUtil = new IdGeneratorUtil();
        for (int i = 0; i < 10; i++) {
            log.info("ID: {}",idGeneratorUtil.snowflakeId(1, 1));
        }
    }

}
