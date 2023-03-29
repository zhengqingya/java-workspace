package com.zhengqing.demo.api;

import com.zhengqing.demo.entity.User;
import com.zhengqing.demo.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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

    @GetMapping("list") // http://127.0.0.1:8080/list
    @ApiOperation("列表")
    public List<User> list() {
        return this.userMapper.selectList(null);
    }

    @GetMapping("map") // http://127.0.0.1:8080/map
    @ApiOperation("返回Map")
    public Object map() {
        Map<Integer, User> map = this.userMapper.selectMap();
        return map;
    }


}
