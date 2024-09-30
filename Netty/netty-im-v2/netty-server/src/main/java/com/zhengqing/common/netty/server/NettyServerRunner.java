package com.zhengqing.common.netty.server;

import com.zhengqing.common.netty.constant.NettyRedisConstant;
import com.zhengqing.common.netty.util.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;

/**
 * <p> netty服务端初始化 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/26 15:40
 */
@Slf4j
@Component
@AllArgsConstructor
public class NettyServerRunner implements CommandLineRunner {

    public static volatile long server_id = 0;
    private final List<NettyServerStrategy> nettyServerList;

    @Override
    public void run(String... args) throws Exception {
        server_id = RedisUtil.incrBy(NettyRedisConstant.MAX_SERVER_ID, 1);
        // 启动服务
        for (NettyServerStrategy nettyServerStrategy : this.nettyServerList) {
            nettyServerStrategy.start();
        }
    }

    @PreDestroy
    public void destroy() {
        // 停止服务
        for (NettyServerStrategy nettyServerStrategy : this.nettyServerList) {
            nettyServerStrategy.stop();
        }
    }

    /**
     * 检查服务器是否已准备好
     *
     * @return 如果服务器已准备好，则返回 true，否则返回 false
     */
    public boolean isReady() {
        for (NettyServerStrategy nettyServerStrategy : this.nettyServerList) {
            if (!nettyServerStrategy.isReady()) {
                return false;
            }
        }
        return true;
    }

}
