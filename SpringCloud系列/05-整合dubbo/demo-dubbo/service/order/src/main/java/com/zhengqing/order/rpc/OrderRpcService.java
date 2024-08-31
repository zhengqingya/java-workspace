package com.zhengqing.order.rpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

@Slf4j
@DubboService
@RequiredArgsConstructor
public class OrderRpcService implements OrderRpcApi {
    @Override
    public Object detail(Long id) {
        log.info("rpc id:{}", id);
        return id;
    }
}
