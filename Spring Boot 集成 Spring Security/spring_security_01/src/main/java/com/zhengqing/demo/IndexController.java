package com.zhengqing.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  <p> 初次体验Spring Security </p>
 *
 * @author：  zhengqing <br/>
 * @date：  2019/9/30$ 9:37$ <br/>
 * @version：  <br/>
 */
@Controller
public class IndexController {

    @GetMapping("/index")
    @ResponseBody
    public String index() {
        return "Hello World ~";
    }

    @RequestMapping("/login")
    public String showLogin() {
        return "login.html";
    }

    @GetMapping("/home")
    @ResponseBody
    public String home() {
        return "欢迎来到主页 ~";
    }

}

