package com.zhengqing.demo.modules.system.service.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhengqing.demo.modules.system.entity.User;
import com.zhengqing.demo.modules.system.mapper.UserMapper;
import com.zhengqing.demo.modules.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p> 系统管理-用户基础信息表 服务实现类 </p>
 *
 * @author: zhengqing
 * @date: 2019-08-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public void listPage(Page<User> page, User filter) {
        page.setRecords(userMapper.selectUsers(page, filter));
    }

    @Override
    public List<User> list(User filter) {
        return userMapper.selectUsers(filter);
    }

    @Override
    public Integer save(User para) {
        if (para.getId()!=null) {
            User user = userMapper.selectById(para.getId());
            userMapper.updateById(para);
        } else {
            userMapper.insert(para);
        }
        return para.getId();
    }

}
