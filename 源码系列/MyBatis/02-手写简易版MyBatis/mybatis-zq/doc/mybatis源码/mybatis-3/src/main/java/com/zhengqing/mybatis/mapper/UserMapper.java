package com.zhengqing.mybatis.mapper;


import com.zhengqing.mybatis.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p> 用户Mapper </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/20 19:06
 */
public interface UserMapper {

  @Select("select * from t_user where id = #{id} and name = #{name}")
  List<User> selectList(@Param("id") Integer id, @Param("name") String name);

}
