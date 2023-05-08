package com.zhengqing.demo.modules.socketio.api;


import com.zhengqing.demo.config.Constants;
import com.zhengqing.demo.modules.common.dto.output.ApiResult;
import com.zhengqing.demo.modules.socketio.server.service.ISocketIOService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> SocketIO测试-接口 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/2/5$ 17:29$
 */
@RestController
@RequestMapping("/api/socket.io")
@Api(tags = "SocketIO测试-接口")
public class SocketIOController {

    @Autowired
    private ISocketIOService socketIOService;

    @PostMapping(value = "/pushMessageToUser", produces = Constants.CONTENT_TYPE)
    @ApiOperation(value = "推送信息给指定客户端", httpMethod = "POST", response = ApiResult.class)
    public ApiResult pushMessageToUser(@RequestParam String userId, @RequestParam String msgContent) {
        socketIOService.pushMessageToUser(userId, msgContent);
        return ApiResult.ok();
    }

}
