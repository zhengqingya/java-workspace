package com.zhengqing.common.netty.server.handler;

import cn.hutool.core.util.StrUtil;
import com.zhengqing.common.netty.constant.NettyRedisConstant;
import com.zhengqing.common.netty.enums.NettyMsgCmdType;
import com.zhengqing.common.netty.model.NettyMsgBase;
import com.zhengqing.common.netty.util.NettyChannelAttrKeyUtil;
import com.zhengqing.common.netty.util.RedisUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <p> 心跳连接 扑通扑通... </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/2/26 18:15
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HeartbeatHandler extends AbstractMsgHandler<String> {

    @Override
    NettyMsgCmdType getMsgCmdEnum() {
        return NettyMsgCmdType.HEART_BEAT;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, String beatInfo) {
        Long heartbeatTimes = NettyChannelAttrKeyUtil.getAttr(ctx.channel(), NettyChannelAttrKeyUtil.HEARTBEAT_TIMES);
        if (heartbeatTimes == null) {
            log.warn("【netty】系统检测到存在未登录连接发送心跳");
            return;
        }

        NettyChannelAttrKeyUtil.setAttr(ctx.channel(), NettyChannelAttrKeyUtil.HEARTBEAT_TIMES, ++heartbeatTimes);
        if (heartbeatTimes % 10 == 0) {
            // 每心跳10次，用户在线状态续一次命
            Long userId = NettyChannelAttrKeyUtil.getAttr(ctx.channel(), NettyChannelAttrKeyUtil.USER_ID);
            Integer terminal = NettyChannelAttrKeyUtil.getAttr(ctx.channel(), NettyChannelAttrKeyUtil.TERMINAL);
            RedisUtil.expire(StrUtil.format("{}:{}:{}", NettyRedisConstant.USER_RE_SERVER_ID, userId, terminal), NettyRedisConstant.HEARTBEAT_TIMEOUT_SECOND, TimeUnit.SECONDS);
        }

        ctx.channel().writeAndFlush(NettyMsgBase.builder().cmd(NettyMsgCmdType.HEART_BEAT.getType()).data("pong").build());
    }

}
