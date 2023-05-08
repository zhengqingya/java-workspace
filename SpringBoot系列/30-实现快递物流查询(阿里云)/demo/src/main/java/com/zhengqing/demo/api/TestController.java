package com.zhengqing.demo.api;


import cn.hutool.core.date.DateTime;
import com.zhengqing.demo.model.dto.LogisticDTO;
import com.zhengqing.demo.model.vo.LogisticVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/5 2:36 下午
 */
@Slf4j
@RestController
@RequestMapping("/test")
@Api(tags = "测试api")
public class TestController {

    @GetMapping("time")
    @ApiOperation("time")
    public String time() {
        log.info("time: {}", DateTime.now());
        return DateTime.now().toString();
    }

    @ApiOperation("查询物流信息")
    @GetMapping("getLogistic")
    public LogisticVO getLogistic(@ModelAttribute LogisticDTO params) {
        return LogisticUtil.getLogisticInfo(params);
    }

}
