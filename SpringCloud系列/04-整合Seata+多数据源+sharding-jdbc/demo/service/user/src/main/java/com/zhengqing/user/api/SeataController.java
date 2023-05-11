package com.zhengqing.user.api;

import cn.hutool.core.date.DateUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.zhengqing.common.core.util.IdGeneratorUtil;
import com.zhengqing.user.entity.User;
import com.zhengqing.user.service.IUserService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p> 分布式事务 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seata")
@Api(tags = {"分布式事务"})
public class SeataController {

    private final IUserService userService;
    private final JdbcTemplate jdbcTemplate;

    @SneakyThrows
    @PostMapping("testSimple")
    @ApiOperation("测试单服务事务")
    @GlobalTransactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.BASE)
    public User testSimple() {
        long id = IdGeneratorUtil.snowflakeId();
        if (id % 2 == 0) {
            id += 1;
        }
        User user = User.builder()
                .userId(id)
                .username("test")
                .password("123456")
                .sex((byte) 1)
                .remark("test data")
                .createTime(new Date())
                .build();
        this.userService.save(user);

        String xid = RootContext.getXID();
        System.err.println("xid:" + xid);
        TimeUnit.SECONDS.sleep(5);

        int a = 1 / 0;
        return user;
    }


    @PostMapping("testSeata")
    @ApiOperation("测试分布式事务")
    public String testSeata() {
        this.userService.testSeata();
        return "OK";
    }

    @SneakyThrows
    @PostMapping("testDynamicDataShource")
    @ApiOperation("测试多数据源")
    @GlobalTransactional(rollbackFor = Exception.class)
    @DS("db-test")
    public void testDynamicDataShource() {
        // db-test 对应的数据源下需要有 t_demo 表
        this.jdbcTemplate.execute(String.format("insert into t_demo (username, password) values ('%s','123456')", DateUtil.now()));

        System.err.println("xid:" + RootContext.getXID());

        // 观察 seata-server 库下的 lock_table 表信息
        TimeUnit.SECONDS.sleep(5);

        int i = 1 / 0;

        // 手动切换数据源
//        DynamicDataSourceContextHolder.push("ds-test");
//        String peek = DynamicDataSourceContextHolder.peek();
//        DynamicDataSourceContextHolder.poll();
    }

    @PostMapping("testLock")
    @GlobalTransactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.BASE)
    @ApiOperation("测试seata全局锁")
    public User testLock() {
        User user = this.userService.detail(1L);
        user.setRemark("更新了...");
        user.updateById(); // 如果在此之前有其它事务在操作，这里拿不到全局锁，会报错：“Caused by: io.seata.rm.datasource.exec.LockConflictException: get global lock fail, xid:172.16.16.88:8091:144503564361254214, lockKeys:t_user:20”
        return user;
    }

}
