package com.zhengqing.demo.modules.chat.api;

import com.zhengqing.demo.config.Constants;
import com.zhengqing.demo.modules.chat.client.service.IChatService;
import com.zhengqing.demo.modules.common.dto.output.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 聊天测试 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/2/5$ 17:29$
 */
@RestController
@RequestMapping("/api")
@Api(tags = "聊天测试-接口")
public class ChatController {

    @Autowired
    private IChatService chatService;

    @PostMapping(value = "/loginConnect", produces = Constants.CONTENT_TYPE)
    @ApiOperation(value = "登陆请求", httpMethod = "POST", response = ApiResult.class)
    public ApiResult loginConnect(@RequestParam String username, @RequestParam String password) {
        chatService.loginConnect(username, password);
        return ApiResult.ok();
    }

    @PostMapping(value = "/sendMsg", produces = Constants.CONTENT_TYPE)
    @ApiOperation(value = "发送消息", httpMethod = "POST", response = ApiResult.class)
    public ApiResult sendMsg(@RequestParam String friendId, @RequestParam String msg) {
        chatService.sendMsg(friendId, msg);
        return ApiResult.ok();
    }

}
