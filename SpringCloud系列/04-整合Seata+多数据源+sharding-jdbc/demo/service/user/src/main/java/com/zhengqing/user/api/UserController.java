package com.zhengqing.user.api;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.zhengqing.user.entity.User;
import com.zhengqing.user.feign.OrderClient;
import com.zhengqing.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Api(tags = {"用户api"})
public class UserController {

    private final IUserService userService;
    private final OrderClient orderClient;

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

    @PostMapping("save")
    @ApiOperation("保存数据")
    public String save(@RequestBody User user) {
        user.setCreateTime(new Date());
        this.userService.saveOrUpdate(user);
        return "OK";
    }

    @PutMapping("update")
    @ApiOperation("更新数据")
    public User update(@RequestParam Long userId) {
        User user = this.userService.detail(userId);
        user.setRemark("更新了...");
        this.userService.updateById(user); // 如果在此之前有其它事务在操作，这里拿不到全局锁，会报错：“Caused by: io.seata.rm.datasource.exec.LockConflictException: get global lock fail, xid:172.16.16.88:8091:144503564361254214, lockKeys:t_user:20”
        return user;
    }


}
