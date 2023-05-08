package com.zhengqing.client.api;

import com.zhengqing.client.schedule.TokenTask;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/4/1 16:02
 */
@Slf4j
@Controller
@RequestMapping("")
@Api(tags = {"测试api"})
public class TestController {

    @Resource
    private TokenTask tokenTask;

    @GetMapping("/index.html")
    public String hello(String code, Model model) {
        model.addAttribute("msg", this.tokenTask.getData(code));
        return "index";
    }

}
