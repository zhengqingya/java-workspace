package com.zhengqing.demo.api;


import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/9/02 17:00
 */
@Slf4j
@RestController
@RequestMapping("api")
@Api(tags = "测试api")
public class TestController {

    /**
     * 如何远程debug调试服务器上的java程序
     * 方式一：java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 ... app.jar
     * 方式二：nginx流量复制
     * 方式三：...
     */
    /**
     * 打印时间
     *
     * @return 字符串时间
     * @author zhengqingya
     */
    @GetMapping("time")
    @ApiOperation("time")
    public String time() {
        log.info("time: {}", DateUtil.date());
        return DateUtil.date().toString();
    }

}
