package com.zhengqing.demo.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhengqing.demo.custom.RequestPostSingleParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 测试api
 * </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2021/1/9 1:38
 */
@Slf4j
@RestController
@RequestMapping("/test")
@Api(tags = "测试api")
public class TestController {

    @PostMapping("")
    @ApiOperation("测试post请求接收单个参数")
    public Integer testPostParam(@ApiParam("id值") @RequestPostSingleParam Integer id) {
        log.info("id: 【{}】", id);
        return id;
    }

}
