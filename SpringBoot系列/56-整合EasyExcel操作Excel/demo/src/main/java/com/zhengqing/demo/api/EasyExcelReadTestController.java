package com.zhengqing.demo.api;

import com.zhengqing.demo.service.IEasyExcelReadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * <p> 测试EasyExcel </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/1/24 17:37
 */
@Slf4j
@RestController
@RequestMapping("/easyExcel/read/test")
@Api(tags = {"EasyExcel-read-api"})
public class EasyExcelReadTestController {

    @Resource
    private IEasyExcelReadService easyExcelReadService;

    @ApiOperation("最简单的读")
    @PostMapping(value = "simpleRead")
    public void simpleRead(@RequestPart @RequestParam MultipartFile file) {
        this.easyExcelReadService.simpleRead(file);
    }

//    @ApiOperation("导出")
//    @GetMapping(value = "/export")
//    public void writeExcel(HttpServletResponse response) {
//        List<ExportModel> list = this.getList();
//        String fileName = "Excel导出测试";
//        String sheetName = "sheet1";
//        EasyExcelUtil.writeExcel(response, list, fileName, sheetName, ExportModel.class);
//    }


}
