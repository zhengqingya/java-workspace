package com.zhengqing.mybatis;

import com.zhengqing.mybatis.entity.User;

import com.zhengqing.mybatis.mapper.UserMapper;
import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * <p> mybatis源码解析$ </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/4/29$ 11:17$
 */
@Slf4j
public class App {

  public static void main(String[] args) throws Exception {
    log.info("====================== ↓↓↓↓↓↓ 1. 从 XML 中构建 SqlSessionFactory ↓↓↓↓↓↓ =======================");
    String resource = "mybatis-config.xml";
    InputStream inputStream = Resources.getResourceAsStream(resource);
    // 拿到数据库源+sql执行语句
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

    log.info("====================== ↓↓↓↓↓↓ 2. 从 SqlSessionFactory 中获取 SqlSession ↓↓↓↓↓↓ =======================");
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      log.info("====================== ↓↓↓↓↓↓ 3. 调用sql ↓↓↓↓↓↓ =======================");
      // 通过全限定名调用sql
      User user = sqlSession.selectOne("com.zhengqing.mybatis.mapper.UserMapper.findOne", 1);
//      UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
//      User user = userMapper.findOne(1);
      System.out.println(user);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (sqlSession != null) {
        sqlSession.close();
      }
    }
  }


}
