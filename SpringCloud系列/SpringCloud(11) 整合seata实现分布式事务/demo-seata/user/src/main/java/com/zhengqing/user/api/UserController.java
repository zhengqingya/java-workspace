package com.zhengqing.user.api;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.zhengqing.user.entity.User;
import com.zhengqing.user.feign.OrderClient;
import com.zhengqing.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户api
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@Api(tags = {"用户api"})
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private OrderClient orderClient;

    @GetMapping("/hello")
    public String hello() {
        return this.orderClient.hello();
    }

    @GetMapping("")
    @ApiOperation("详情")
    @DS("db-test")
    public User detail(@RequestParam Long userId) {
        return this.userService.detail(userId);
    }

    @PostMapping("")
    @ApiOperation("保存数据")
//    @GlobalTransactional(name = "db-user", rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
//    @Transactional(rollbackFor = Exception.class)
    public String insertData(@RequestBody User user) {
        this.userService.addOrUpdateData(user);
        return "OK";
    }

    @PostMapping("testDynamicDataShource")
    @ApiOperation("测试多数据源")
    @Transactional(rollbackFor = Exception.class)
//    @DSTransactional
    @DS("db-test")
    public void testDynamicDataShource(@RequestBody User user) {
        boolean b = this.userService.saveOrUpdate(user);

//        int i = 1 / 0;

        //手动切换
//        DynamicDataSourceContextHolder.push(UcmsDataSourceConstant.CALLING);
//        String peek = DynamicDataSourceContextHolder.peek();
//        DynamicDataSourceContextHolder.poll();
    }


}
