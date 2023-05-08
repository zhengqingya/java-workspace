package com.zhengqing.demo.modules.system.api;

import com.zhengqing.demo.modules.common.api.BaseController;
import com.zhengqing.demo.modules.common.dto.output.ApiResult;
import com.zhengqing.demo.modules.system.service.ITableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@Api(tags = "系统管理-数据库表管理")
public class TableController extends BaseController {

    @Autowired
    ITableService tableService;

    @GetMapping(value = "/tableToWord", produces = "application/json;charset=utf-8", name = "导出数据库表信息生成Word")
    @ApiOperation(value = "导出数据库表信息生成Word", httpMethod = "GET", response = ApiResult.class, notes = "导出数据库表信息生成Word")
    public ApiResult tableToWord() {
        try {
            return ApiResult.ok("导出数据库表信息生成Word成功", tableService.getTableInfo());
        } catch (Exception e) {
            log.error("导出数据库表信息生成Word失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }
}
