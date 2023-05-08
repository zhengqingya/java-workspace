package com.zhengqing.demo.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/9/15 16:08
 */
@Slf4j
@RestController
@RequestMapping("")
@Api(tags = "测试-接口")
public class IndexController {

    private final String KEY = "test-key";

    @Autowired
//    @Resource(name = "redisTemplate")
    private StringRedisTemplate redisTemplate;

    @Resource(name = "redisTemplate2")
    private StringRedisTemplate redisTemplate2;

    @PostMapping("redis")
    @ApiOperation("redis-保存数据")
    public String saveData(String key) {
        this.redisTemplate.opsForValue().set(key, "hello world - reids");
        return "SUCCESS";
    }

    @GetMapping("redis")
    @ApiOperation("redis-获取数据")
    public String getData(String key) {
        String dataStr = this.redisTemplate.opsForValue().get(key);
        log.info("{}", dataStr);
        return dataStr;
    }


    @PostMapping("redis2")
    @ApiOperation("redis2-保存数据")
    public String saveData2(String key) {
        this.redisTemplate2.opsForValue().set(key, "hello world - reids2");
        return "SUCCESS";
    }

    @GetMapping("redis2")
    @ApiOperation("redis2-获取数据")
    public String getData2(String key) {
        String dataStr = this.redisTemplate2.opsForValue().get(key);
        log.info("{}", dataStr);
        return dataStr;
    }

}
