package com.zhengqing.demo.api;


import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.date.DateTime;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/5 2:36 下午
 */
@Slf4j
@RestController
@RequestMapping("/test")
@Api(tags = "测试api")
public class TestController {

    @GetMapping("time")
    @ApiOperation("time")
    public String time() {
        log.info("time: {}", DateTime.now());
        return DateTime.now().toString();
    }

    @GetMapping("getSaTokenConfig")
    @ApiOperation("Sa-Token配置")
    public SaTokenConfig getSaTokenConfig() {
        log.info("Sa-Token配置：{}", SaManager.getConfig());
        return SaManager.getConfig();
    }

    @GetMapping("doLogin")
    @ApiOperation("登录")
    public String doLogin(String username, String password) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        if ("zhang".equals(username) && "123456".equals(password)) {
            StpUtil.logout();
            StpUtil.login(10001);
            return "登录成功" + StpUtil.getLoginId();
        }
        StpUtil.logout();
        return "登录失败";
    }

    @GetMapping("isLogin")
    @ApiOperation("查询登录状态")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

    @GetMapping("logout")
    @ApiOperation("退出登录")
    public String logout(String loginId) {
        StpUtil.logoutByLoginId(loginId);
        return "SUCCESS";
    }

    @GetMapping("checkLogin")
    @ApiOperation("检查是否登录")
    public String checkLogin() {
        try {
            StpUtil.checkLogin();
        } catch (Exception e) {
            log.info("登录认证失效：{}", e.getMessage());
            return "FAIL:" + e.getMessage();
        }
        log.info("登录了...");
        return "SUCCESS";
    }

    @GetMapping("tokenInfo")
    @ApiOperation("查询Token信息")
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

    @GetMapping("getTokenValueByLoginId")
    @ApiOperation("获取账号id为10001的token令牌值")
    public String getTokenValueByLoginId() {
        return StpUtil.getTokenValueByLoginId(10001);
    }

}
