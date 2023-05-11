package com.zhengqing.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhengqing.user.entity.User;
import com.zhengqing.user.feign.OrderClient;
import com.zhengqing.user.mapper.UserMapper;
import com.zhengqing.user.service.IUserService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @GlobalTransactional(rollbackFor = Exception.class)
    public void testSeata() {
        this.orderClient.insertData();

        this.addData();
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public void addData() {
        this.save(
                User.builder()
                        .username("test")
                        .password("123456")
                        .date(new Date())
                        .build()
        );
//        TimeUnit.SECONDS.sleep(10); // 测试其它事务去修改数据时的全局锁
        int i = 1 / 0;
    }

}
