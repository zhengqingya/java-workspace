package com.zhengqing.demo;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhengqing.demo.modules.system.entity.User;
import com.zhengqing.demo.modules.system.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests2 {

    // 原生mybatis方式

    @Autowired
    IUserService userService;

    /**
     * 查询所有数据 - 原生mybatis方式
     */
    @Test
    public void testSelectAll() throws Exception{
        List<User> users = userService.list(new User());
        System.out.println(users);
    }

    /**
     * 查询所有数据-分页
     */
    @Test
    public void testSelectAllPage() throws Exception{
        Page<User> page = new Page<>(1, 10);
        userService.listPage(page, new User());
        System.out.println( page );
    }

    /**
     * 新增或修改数据 - 有id则更新，无则新增
     */
    @Test
    public void testSave() throws Exception{
        User user = new User();
//        user.setId(1);
        user.setUsername("test");
        user.setPassword("123456");
        user.setNickName("测试号");
        userService.save(user);
    }

}
