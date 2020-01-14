package com.zhengqing.demo.modules.system.api;

import com.zhengqing.demo.modules.common.api.BaseController;
import com.zhengqing.demo.modules.common.dto.output.ApiResult;
import com.zhengqing.demo.modules.common.validator.NoRepeatSubmit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *  <p> 测试 - 控制层 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/11/27 12:44
 */
@RestController
public class IndexController extends BaseController {

    @NoRepeatSubmit
    @GetMapping(value = "/index", produces = "application/json;charset=utf-8")
    public ApiResult index() {
        return ApiResult.ok("Hello World ~ ");
    }

}
