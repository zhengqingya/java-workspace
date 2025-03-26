package com.zhengqing.demo.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogService {
    public void _mock_log() {
        if (log.isDebugEnabled()) {
            log.debug("debug log...");
            System.out.println("debug log...");
        }
        System.out.println("come...");
    }
}