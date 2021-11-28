package com.zhengqing.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhengqing.user.entity.User;
import com.zhengqing.user.feign.OrderClient;
import com.zhengqing.user.mapper.UserMapper;
import com.zhengqing.user.service.IUserService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @GlobalTransactional
    public void addOrUpdateData(User user) {
        this.orderClient.insertData();
        this.addData(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addData(User user) {
        user.insertOrUpdate();
        int i = 1 / 0;
    }

}
