package com.zhengqing.demo.modules.system.api;

import com.zhengqing.demo.modules.common.api.BaseController;
import com.zhengqing.demo.modules.common.dto.output.ApiResult;
import com.zhengqing.demo.modules.common.validator.NoRepeatSubmit;
import com.zhengqing.demo.modules.system.service.ILogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Random;


/**
 * <p> 测试 - 控制层 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2019/11/27 12:44
 */
@RestController
@RequestMapping("/api")
@Api(description = "测试-接口")
public class IndexController extends BaseController {

    @Autowired
    private ILogService logService;

    @GetMapping(value = "/export", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "导出", httpMethod = "GET", response = ApiResult.class)
    public void export(HttpServletResponse response) {
        this.logService.export(response, "日志信息" + new Random().nextInt(1000));
    }

    @NoRepeatSubmit
    @GetMapping(value = "/index", produces = "application/json;charset=utf-8")
    public ApiResult index() {
        return ApiResult.ok("Hello World ~ ");
    }

}
