package com.zhengqing.demo.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhengqing.demo.entity.Demo;
import com.zhengqing.demo.model.dto.DemoListDTO;
import com.zhengqing.demo.model.dto.DemoSaveDTO;
import com.zhengqing.demo.model.vo.DemoListVO;
import com.zhengqing.demo.service.IDemoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p> 测试demo 接口 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/web/api/demo")
@Api(tags = {"测试demo接口"})
public class DemoController {

    private final IDemoService demoService;

    @GetMapping("page")
    @ApiOperation("列表分页")
    public IPage<DemoListVO> listPage(@ModelAttribute DemoListDTO params) {
        return this.demoService.listPage(params);
    }

    @GetMapping("list")
    @ApiOperation("列表")
    public List<DemoListVO> list(@ModelAttribute DemoListDTO params) {
        return this.demoService.list(params);
    }

    @PostMapping("")
    @ApiOperation("新增")
    public Integer add(@Validated @RequestBody DemoSaveDTO params) {
        return this.demoService.addOrUpdateData(params);
    }

    @PutMapping("")
    @ApiOperation("更新")
    public Integer update(@Validated @RequestBody DemoSaveDTO params) {
        return this.demoService.addOrUpdateData(params);
    }

    @DeleteMapping("")
    @ApiOperation("删除")
    public void delete(@RequestParam Integer id) {
        this.demoService.removeById(id);
    }

    @GetMapping("detail")
    @ApiOperation("详情")
    public Demo detail(@RequestParam Integer id) {
        return this.demoService.getById(id);
    }

}
