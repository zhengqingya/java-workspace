package com.zhengqing.client1.api;

import cn.hutool.core.date.DateTime;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/4/1 16:02
 */
@Slf4j
@RestController
@RequestMapping("")
@Api(tags = {"测试api"})
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        log.info("client1: {}", DateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String result = "[client1] " + authentication.getName() + Arrays.toString(authentication.getAuthorities().toArray());
        log.info("client1-result: {}", result);
        return result;
    }

}
