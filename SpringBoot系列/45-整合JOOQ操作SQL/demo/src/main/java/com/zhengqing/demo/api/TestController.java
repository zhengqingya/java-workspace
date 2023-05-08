package com.zhengqing.demo.api;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.zhengqing.demo.dsl.jooq.Tables.T_USER;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/5 2:36 下午
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("")
@Api(tags = "测试api")
public class TestController {

    private DSLContext dslContext;


    @GetMapping("time")
    @ApiOperation("time")
    public String time() {
        log.info("time: {}", DateUtil.date());
        return DateUtil.date().toString();
    }

    @GetMapping("sql")
    @ApiOperation("sql")
    public Object sql() {

        // 插入数据
        this.dslContext.insertInto(T_USER, T_USER.USERNAME, T_USER.NICKNAME)
                .values(RandomUtil.randomString(10), RandomUtil.randomString(10))
                .execute();

        return "OK";
    }

}
