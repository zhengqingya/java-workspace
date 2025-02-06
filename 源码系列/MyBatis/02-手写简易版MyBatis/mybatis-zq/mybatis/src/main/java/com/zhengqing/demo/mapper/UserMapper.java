package com.zhengqing.demo.mapper;

import com.zhengqing.demo.entity.User;
import com.zhengqing.mybatis.annotations.*;

import java.util.List;

/**
 * <p> 用户Mapper </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/20 19:06
 */
@CacheNamespace
public interface UserMapper {

    User findOne(@Param("id") Integer id);

    @Select("select * from t_user where id = #{id} and name = #{name}")
    List<User> selectList(@Param("id") Integer id, @Param("name") String name);

    @Select("select * from t_user where id = #{id}")
    User selectOne(@Param("id") Integer id);

    @Insert("insert into t_user(name, age) values(#{user.name}, #{user.age})")
    Long insert(@Param("user") User user);

    @Delete("delete from t_user where id = #{id}")
    Long delete(@Param("id") Integer id);

    @Update("update t_user set name = #{name} where id = #{id}")
    Integer update(@Param("id") Integer id, @Param("name") String name);

}
