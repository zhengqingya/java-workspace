package com.zhengqing.spring.hello.service;

import com.zhengqing.spring.annotation.Autowired;
import com.zhengqing.spring.annotation.Component;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DbInitializer {

    @Autowired
    UserService userService;

    @PostConstruct
    void init() {
        log.info("init database...");
        userService.initDb();
    }
}
