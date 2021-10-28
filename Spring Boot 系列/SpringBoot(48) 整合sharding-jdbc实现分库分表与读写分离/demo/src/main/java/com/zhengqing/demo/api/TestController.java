package com.zhengqing.demo.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhengqing.demo.entity.User;
import com.zhengqing.demo.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 测试api
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
@Slf4j
@RestController
@RequestMapping("/web/api/test")
@Api(tags = {"测试api"})
public class TestController {

    @Autowired
    private IUserService demoService;

    @GetMapping("list/page")
    @ApiOperation("列表分页")
    public IPage<User> listPage(@ModelAttribute User params) {
        return this.demoService.listPage(params);
    }

    @PostMapping("")
    @ApiOperation("新增")
    public Long add(@Validated @RequestBody User params) {
        return this.demoService.addOrUpdateData(params);
    }

    @PutMapping("")
    @ApiOperation("更新")
    public Long update(@Validated @RequestBody User params) {
        return this.demoService.addOrUpdateData(params);
    }

    @PostMapping("add/batch/data")
    @ApiOperation("测试插入数据用时")
    public String addBatchData(@ApiParam(value = "插入数据量", required = true, example = "10000")
                               @RequestParam int addSum) {
        return this.demoService.addBatchData(addSum);
    }

}
