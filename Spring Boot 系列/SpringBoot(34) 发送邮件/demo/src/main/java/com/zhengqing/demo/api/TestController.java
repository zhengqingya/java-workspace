package com.zhengqing.demo.api;

import java.io.File;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhengqing.demo.utils.EmailUtil;

/**
 * <p>
 * 测试接口
 * </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/9/9 10:22
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/sendEmailTest")
    public String sendEmailTest() {
        EmailUtil.sendMail("测试发送普通邮件", "test...", "xxx@qq.com");
        File file = new File("C:\\Users\\Administrator\\Desktop\\临时垃圾站\\test.docx");
        EmailUtil.sendMail("测试发送带附件邮件", "test...", "test.docx", file, "xxx@qq.com");
        return "SUCCESS ~";
    }

}
