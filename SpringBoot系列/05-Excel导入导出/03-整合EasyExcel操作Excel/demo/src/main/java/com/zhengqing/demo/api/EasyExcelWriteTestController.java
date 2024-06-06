package com.zhengqing.demo.api;

import com.zhengqing.demo.service.IEasyExcelWriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * <p> 测试EasyExcel </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/1/24 17:37
 */
@Slf4j
@RestController
@RequestMapping("/easyExcel/write/test")
@Api(tags = {"EasyExcel-write-api"})
public class EasyExcelWriteTestController {

    @Resource
    private IEasyExcelWriteService easyExcelWriteService;

    @ApiOperation("最简单的写")
    @PostMapping(value = "simpleRead")
    public void simpleRead() {
        this.easyExcelWriteService.simpleWrite();
    }

    @ApiOperation("合并单元格")
    @GetMapping(value = "mergeWrite")
    public void mergeWrite() {
        this.easyExcelWriteService.mergeWrite();
    }

    @ApiOperation("web中的写")
    @GetMapping(value = "download")
    public void download(HttpServletResponse response) {
        this.easyExcelWriteService.download(response);
    }

    @ApiOperation("web中的写并且失败的时候返回json")
    @GetMapping(value = "downloadFailedUsingJson")
    public void downloadFailedUsingJson(HttpServletResponse response) {
        this.easyExcelWriteService.downloadFailedUsingJson(response);
    }


    @ApiOperation("动态合并单元格")
    @GetMapping(value = "dynamicMergeData") // http://127.0.0.1/easyExcel/write/test/dynamicMergeData
    public void dynamicMergeData(HttpServletResponse response) {
        this.easyExcelWriteService.dynamicMergeData(response);
    }


}
