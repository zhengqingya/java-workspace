package com.zhengqing.demo.modules.weixin.api;


import com.zhengqing.demo.config.Constants;
import com.zhengqing.demo.modules.common.api.BaseController;
import com.zhengqing.demo.modules.common.dto.output.ApiResult;
import com.zhengqing.demo.modules.weixin.model.menu.Menu;
import com.zhengqing.demo.modules.weixin.model.WeixinResponseResult;
import com.zhengqing.demo.modules.weixin.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p> 微信菜单 - 接口 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2019/6/10 14:16
 */
@Slf4j
@RestController
@RequestMapping("/api/weixin/menu")
@Api(tags = "微信菜单 - 接口")
public class MenuController extends BaseController {

    @Autowired
    private IMenuService menuService;

    @GetMapping(value = "/getMenu", produces = Constants.CONTENT_TYPE)
    @ApiOperation(value = "查询菜单", httpMethod = "GET", response = ApiResult.class, notes = "查询菜单")
    public ApiResult getMenu(@RequestParam("accessToken") String accessToken) {
        Object result = menuService.getMenu(accessToken);
        return ApiResult.ok("查询菜单成功！", result);
    }

    @GetMapping(value = "/deleteMenu", produces = Constants.CONTENT_TYPE)
    @ApiOperation(value = "删除菜单", httpMethod = "GET", response = ApiResult.class, notes = "删除菜单")
    public ApiResult deleteMenu(@RequestParam("accessToken") String accessToken) {
        WeixinResponseResult result = menuService.deleteMenu(accessToken);
        // 删除失败处理
        if (result != null && result.getErrcode() != 0) {
            return ApiResult.fail(result.getErrcode(), result.getErrmsg());
        }
        return ApiResult.ok(result);
    }

    @PostMapping(value = "/createMenu", produces = Constants.CONTENT_TYPE)
    @ApiOperation(value = "创建菜单", httpMethod = "POST", response = ApiResult.class, notes = "创建菜单")
    public ApiResult createMenu(@RequestBody Menu menu, @RequestParam("accessToken") String accessToken) {
        WeixinResponseResult result = menuService.createMenu(menu, accessToken);
        // 创建失败处理
        if (result != null && result.getErrcode() != 0) {
            return ApiResult.fail(result.getErrcode(), result.getErrmsg());
        }
        return ApiResult.ok(result);
    }

}
