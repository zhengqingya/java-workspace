package com.zhengqing.demo;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.zhengqing.demo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;

/**
 * <p> 测试类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/4/21 17:06
 */
@Slf4j
public class AppTest {

    @Test
    public void test() throws Exception {
        // 创建一个数据源
        SimpleDataSource dataSource = new SimpleDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1/demo?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        // 创建一个NutDao实例,在真实项目中, NutDao通常由ioc托管, 使用注入的方式获得.
        Dao dao = new NutDao(dataSource);

        // 创建表
        dao.create(User.class, false); // false的含义是,如果表已经存在,就不要删除重建了.

        // 插入数据
        dao.insert(User.builder()
                .username(RandomUtil.randomString(10))
                .nickname(RandomUtil.randomString(10))
                .build());

        // 查询数据
        User user = dao.fetch(User.class, Cnd.where("id", "=", "1"));
        System.out.println(JSONUtil.toJsonStr(user));
    }

}
