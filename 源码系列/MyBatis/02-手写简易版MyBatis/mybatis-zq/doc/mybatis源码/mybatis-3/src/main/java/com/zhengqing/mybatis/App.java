package com.zhengqing.mybatis;

import cn.hutool.json.JSONUtil;
import com.zhengqing.mybatis.entity.User;
import com.zhengqing.mybatis.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>  </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/29 07:17
 */
public class App {

  public static void main(String[] args) throws IOException {
    String resource = "mybatis-config.xml";
    InputStream inputStream = Resources.getResourceAsStream(resource);
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    SqlSession sqlSession = sqlSessionFactory.openSession();
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    List<User> userList = userMapper.selectList(1, "zq");
    System.out.println(JSONUtil.toJsonStr(userList));
  }

}
