package com.zhengqing.demo.api;


import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/4/1 16:02
 */
@Slf4j
@RestController
@RequestMapping("")
@Api(tags = "测试api")
public class TestController {

    @GetMapping("time")
    public String time() {
        log.info("time: {}", DateTime.now());
        return DateTime.now().toString();
    }

    /**
     * 应用申请地址 => https://gitee.com/oauth/applications
     * http://127.0.0.1:8080/oauth/gitee
     */
    @SneakyThrows(Exception.class)
    @GetMapping("/oauth/gitee")
    public String callback(HttpServletResponse response) {
        AuthRequest authRequest = this.getAuthRequest();
        String redirectUrl = authRequest.authorize(AuthStateUtils.createState());
        response.sendRedirect(redirectUrl);
        log.info("《三方授权》 重定向url：{}", redirectUrl);
        return redirectUrl;
    }

    @SneakyThrows(Exception.class)
    @GetMapping("/oauth/gitee/callback")
    public Object authorization_code(AuthCallback callback, HttpServletResponse response) {
        AuthRequest authRequest = this.getAuthRequest();
        AuthResponse authResponse = authRequest.login(callback);
        int code = authResponse.getCode();
        if (code != 2000) {
            throw new Exception("《三方授权》 回调异常： " + authResponse.getMsg());
        }
        Object authResponseData = authResponse.getData();
        log.debug("《三方授权》 授权信息：{}", JSONObject.toJSONString(authResponseData));
        return authResponseData;
    }

    private AuthRequest getAuthRequest() {
        return new AuthGiteeRequest(AuthConfig.builder()
                .clientId("xxx")
                .clientSecret("xxx")
                .redirectUri("http://127.0.0.1:8080/oauth/gitee/callback")
                .build());
    }

}
