package com.zhengqing.mybatis.session;

/**
 * <p> 生产SqlSession </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/23 01:57
 */
public interface SqlSessionFactory {

    SqlSession openSession();

    SqlSession openSession(boolean autoCommit);

}
