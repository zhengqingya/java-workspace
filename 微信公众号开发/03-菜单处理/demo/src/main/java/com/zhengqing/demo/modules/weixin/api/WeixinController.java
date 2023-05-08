package com.zhengqing.demo.modules.weixin.api;


import com.zhengqing.demo.config.Constants;
import com.zhengqing.demo.modules.common.api.BaseController;
import com.zhengqing.demo.modules.common.dto.output.ApiResult;
import com.zhengqing.demo.modules.weixin.model.AccessTokenVO;
import com.zhengqing.demo.modules.weixin.service.IWeixinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 微信授权 - 接口 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2019/6/10 14:16
 */
@Slf4j
@RestController
@RequestMapping("/api/weixin/basic")
@Api(tags = "微信授权 - 接口")
public class WeixinController extends BaseController {

    @Autowired
    private IWeixinService weixinService;

    @GetMapping(value = "/getAccessToken", produces = Constants.CONTENT_TYPE)
    @ApiOperation(value = "根据AppID和AppSecret获取access_token", httpMethod = "GET", response = ApiResult.class, notes = "根据AppID和AppSecret获取access_token")
    public ApiResult getAccessToken(@RequestParam("appId") String appId, @RequestParam("appSecret") String appSecret) {
        AccessTokenVO result = weixinService.getAccessToken(appId, appSecret);
        return ApiResult.ok("根据AppID和AppSecret获取access_token成功！", result);
    }

}
