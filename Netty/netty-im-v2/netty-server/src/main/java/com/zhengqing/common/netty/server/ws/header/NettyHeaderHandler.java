package com.zhengqing.common.netty.server.ws.header;

import cn.hutool.json.JSONUtil;
import com.zhengqing.common.netty.model.NettyJwtUser;
import com.zhengqing.common.netty.util.JwtUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * <p> ws子协议传递token Sec-Websocket-Protocol 解析处理 </p>
 *
 * @author zhengqingya
 * @description 最后得返回同样的 Sec-Websocket-Protocol 响应才行，不然会断开连接
 * tips: 可以放到握手完成后获取请求头信息
 * @date 2024/2/26 10:11
 */
@Slf4j
public class NettyHeaderHandler extends ChannelInboundHandlerAdapter {

    @Value("${jwt.accessToken.secret}")
    private String accessTokenSecret;

    @Value("${netty.web-socket.custom-header.enable:false}")
    private Boolean isCustomHeader = false;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!isCustomHeader) {
            return;
        }
        if (msg instanceof FullHttpRequest) {
            HttpRequest request = (FullHttpRequest) msg;
            HttpHeaders headers = request.headers();
            String token = headers.get("Sec-Websocket-Protocol");
            log.debug("【netty-header】 token:{}",token);
            if (!JwtUtil.checkSign(token, this.accessTokenSecret)) {
                log.warn("【netty-header】用户token：{} 校验不通过，强制下线", token);
                ctx.channel().close();
                return;
            }
            NettyJwtUser nettyJwtUser = JwtUtil.get(token);
            log.info("【netty-header】用户信息: {}", JSONUtil.toJsonStr(nettyJwtUser));
        } else {
//            log.debug("【netty-header】: {}", msg);
        }
        ctx.fireChannelRead(msg);
    }

}