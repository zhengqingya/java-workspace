package com.zhengqing.demo.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 首页 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/8/9 14:23
 */
@RestController
public class IndexController {

    /**
     * http://127.0.0.1:8080/index
     */
    @RequestMapping("index")
    public String index() {
        return "index";
    }

}
