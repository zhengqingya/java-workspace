package com.zhengqing.mybatis.demo.mapper;

import com.zhengqing.mybatis.annotations.CacheNamespace;
import com.zhengqing.mybatis.annotations.Insert;
import com.zhengqing.mybatis.annotations.Param;
import com.zhengqing.mybatis.annotations.Select;
import com.zhengqing.mybatis.demo.entity.User;

/**
 * <p> 用户Mapper </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/20 19:06
 */
@CacheNamespace
public interface UserMapper {

    @Select("select * from t_user where id = #{id}")
    User findOne(@Param("id") Integer id);

    @Insert("insert into t_user(name, age) values(#{user.name}, #{user.age})")
    Long insert(@Param("user") User user);

}
