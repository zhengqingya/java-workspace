package com.zhengqing.demo.modules.weixin.api;


import com.zhengqing.demo.modules.common.api.BaseController;
import com.zhengqing.demo.modules.weixin.service.IMsgService;
import com.zhengqing.demo.utils.SecurityUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * <p> 微信 - 接口 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2019/6/10 14:16
 */
@Slf4j
@RestController
@RequestMapping("/api/weixin/index")
@Api(tags = "微信 - 接口")
public class IndexController extends BaseController {

    @Autowired
    private IMsgService msgService;

    // TODO 这里的token是微信公众平台上自己所配的！
    private static final String token = "zhengqing";

    /**
     * 处理微信认证：验证服务器地址的有效性，get提交
     * signature: 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * timestamp 时间戳
     * nonce: 随机数
     * echostr: 随机字符串
     */
    @GetMapping
    public void checkSignature(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("============= 处理微信认证 ===============");
        // 拿到微信的请求参数
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        // ① 将token、timestamp、nonce三个参数进行字典序排序 b a d c h ==>a b c d h
        String[] strArr = {token, timestamp, nonce};
        // 字典排序
        Arrays.sort(strArr);
        // ② 将三个参数字符串拼接成一个字符串进行sha1加密
        StringBuffer sb = new StringBuffer();
        // 字符串拼接
        for (String str : strArr) {
            sb.append(str);
        }
        // 加密
        String sha1Str = SecurityUtil.sha1(sb.toString());
        // ③ 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        PrintWriter out = response.getWriter();
        if (sha1Str.equals(signature)) {
            // 如果相等，就是来自微信请求
            // 若确认此次GET请求来自微信服务器，原样返回echostr参数内容，则接入生效
            out.println(echostr);
        }
        out.close();
        out = null;
    }

    /**
     * 解析请求消息，post请求
     */
    @PostMapping
    public void msgProcess(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("============= 消息来了 =============");
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        // 调用核心业务类接收消息、处理消息
        String respMessage = msgService.processRequest(request);
//        String respMessage = msgService.processRequestReturnSameMsg(request);
        // 响应消息
        PrintWriter out = response.getWriter();
        out.print(respMessage);
        out.close();
    }


    /**
     * 解析请求消息，post请求
     */
//    @PostMapping
//    public void msgProcess(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        // 获取请求的字节流
//        ServletInputStream inputStream = request.getInputStream();
//        // 转换为字符流， 得到缓冲流
//        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
//        BufferedReader reader = new BufferedReader(inputStreamReader);
//        String content = null;
//        // 读取消息内容
//        while ((content = reader.readLine()) != null) {
//            System.out.println(content);
//        }
//    }

}
