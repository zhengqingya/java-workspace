package com.zhengqing.mybatis.demo.service.impl;

import com.zhengqing.mybatis.demo.entity.User;
import com.zhengqing.mybatis.demo.mapper.TestMapper;
import com.zhengqing.mybatis.demo.mapper.UserMapper;
import com.zhengqing.mybatis.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p> 用户 实现类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/5/5 04:05
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User findOne(Integer id) {
        System.out.println("UserServiceImpl.findOne");
        User one = this.userMapper.findOne(id);
        User one1 = this.testMapper.findOne(id);
        return one;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(User user) {
        System.out.println("UserServiceImpl.save");
        this.userMapper.insert(user);
//        this.jdbcTemplate.execute("insert into t_user( name, age ) values('zhengqingya', 18)");
//        int a = 1 / 0;
    }

}
