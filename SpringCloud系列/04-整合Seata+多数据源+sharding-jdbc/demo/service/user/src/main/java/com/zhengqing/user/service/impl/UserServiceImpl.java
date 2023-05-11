package com.zhengqing.user.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhengqing.common.core.util.IdGeneratorUtil;
import com.zhengqing.common.db.config.dynamic.DataSourceConfig;
import com.zhengqing.user.entity.User;
import com.zhengqing.user.feign.OrderClient;
import com.zhengqing.user.mapper.UserMapper;
import com.zhengqing.user.service.IUserService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
@Slf4j
@Service
@DS(DataSourceConfig.SHARDING_DATA_SOURCE_NAME)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderClient orderClient;

    @Override
    public User detail(Long userId) {
        return this.userMapper.selectById(userId);
    }

    @Override
//    @GlobalTransactional(name = "db-user",
//            rollbackFor = Exception.class,
//            propagation = Propagation.REQUIRES_NEW)

    // Sharding+Seata分布式事务
    @GlobalTransactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.BASE)
    public void testSeata() {
        this.orderClient.insertData();

        this.addData();
    }

    @SneakyThrows
    public void addData() {
        long id = IdGeneratorUtil.snowflakeId();
        if (id % 2 == 0) {
            id += 1;
        }
        this.save(
                User.builder()
                        .userId(id)
                        .username("test")
                        .password("123456")
                        .createTime(new Date())
                        .build()
        );
//        TimeUnit.SECONDS.sleep(10); // 测试其它事务去修改数据时的全局锁
        int i = 1 / 0;
    }

}
