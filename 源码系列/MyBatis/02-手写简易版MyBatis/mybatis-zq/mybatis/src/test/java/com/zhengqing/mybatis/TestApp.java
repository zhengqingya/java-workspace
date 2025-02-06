package com.zhengqing.mybatis;

import cn.hutool.json.JSONUtil;
import com.zhengqing.demo.mapper.UserMapper;
import com.zhengqing.mybatis.session.SqlSession;
import com.zhengqing.mybatis.session.SqlSessionFactory;
import com.zhengqing.mybatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

/**
 * <p> 测试 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/20 19:22
 */
public class TestApp {

    @Test
    public void test() throws Exception {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build();
        SqlSession sqlSession = sqlSessionFactory.openSession(false);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        System.out.println(JSONUtil.toJsonStr(userMapper.findOne(1)));
//        System.out.println(JSONUtil.toJsonStr(userMapper.selectList(1, "zq")));
//        System.out.println(JSONUtil.toJsonStr(userMapper.selectList(1, "zq")));

//        UserMapper userMapper2 = sqlSessionFactory.openSession().getMapper(UserMapper.class);
//        System.out.println(JSONUtil.toJsonStr(userMapper2.selectList(1, "zq")));
//        System.out.println(JSONUtil.toJsonStr(userMapper2.selectList(2, "xx")));

//        System.out.println(userMapper.selectOne(1));
//        System.out.println(userMapper.insert(User.builder().name(RandomUtil.randomString(5)).age(RandomUtil.randomInt(1, 100)).build()));
//        System.out.println(userMapper.delete(5));
//        System.out.println(userMapper.update(2, "xxx"));
//        System.out.println(JSONUtil.toJsonStr(userMapper.selectList(1, "zq")));
        sqlSession.commit();
        sqlSession.close();
    }

}
