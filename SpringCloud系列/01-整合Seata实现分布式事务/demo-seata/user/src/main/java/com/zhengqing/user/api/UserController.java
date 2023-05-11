package com.zhengqing.user.api;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.zhengqing.user.entity.User;
import com.zhengqing.user.feign.OrderClient;
import com.zhengqing.user.service.IUserService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    private final DataSource dataSource;

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

    @PutMapping("update")
    @ApiOperation("更新数据")
    @GlobalTransactional
    public User update(@RequestParam Long userId) {
        User user = this.userService.detail(userId);
        user.setRemark("更新了...");
        user.updateById(); // 如果在此之前有其它事务在操作，这里拿不到全局锁，会报错：“Caused by: io.seata.rm.datasource.exec.LockConflictException: get global lock fail, xid:172.16.16.88:8091:144503564361254214, lockKeys:t_user:20”
        return user;
    }

    @PostMapping("testSeata")
    @ApiOperation("测试分布式事务")
//    @GlobalTransactional(name = "db-user", rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
//    @Transactional(rollbackFor = Exception.class)
    public String testSeata() {
        this.userService.testSeata();
        return "OK";
    }

    @SneakyThrows
    @PostMapping("testDynamicDataShource")
    @ApiOperation("测试多数据源")
    @GlobalTransactional
    @DS("db-test")
    public void testDynamicDataShource() {
        this.userService.save(
                User.builder()
                        .username("test")
                        .password("123456")
                        .date(new Date())
                        .build()
        );
        System.err.println("xid:" + RootContext.getXID());

        // 观察 seata-server 库下的 lock_table 表信息
        TimeUnit.SECONDS.sleep(5);

        int i = 1 / 0;

        // 手动切换数据源
//        DynamicDataSourceContextHolder.push("ds-test");
//        String peek = DynamicDataSourceContextHolder.peek();
//        DynamicDataSourceContextHolder.poll();
    }


}
