package com.zhengqing.demo.mapper;

import com.zhengqing.demo.entity.User;
import com.zhengqing.mybatis.annotations.Param;
import com.zhengqing.mybatis.annotations.Select;

/**
 * <p> 测试Mapper </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/20 19:06
 */
public interface TestMapper {

    @Select("select * from t_user where id = #{id}")
    User selectOne(@Param("id") Integer id);

}
