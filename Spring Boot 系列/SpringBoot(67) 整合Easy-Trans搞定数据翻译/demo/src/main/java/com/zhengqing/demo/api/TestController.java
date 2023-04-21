package com.zhengqing.demo.api;

import com.fhs.trans.service.impl.TransService;
import com.zhengqing.demo.entity.User;
import com.zhengqing.demo.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/3/29 13:54
 */
@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Api(tags = "测试api")
public class TestController {

    private final UserMapper userMapper;
    private final TransService transService;

    @GetMapping("test")
    @ApiOperation("测试")
//    @TransMethodResult // 手动翻译 -- 方式1
    public Object test() {
        User user = this.userMapper.selectById(1);
//        this.transService.transOne(user); // 手动翻译 -- 方式2
        return user;
    }


}
