package com.zhengqing.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhengqing.demo.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<User> {

    @Select("select username from t_test where id = #{id}")
    String selectNameById(@Param("id") Integer id);

    @Select("select id from t_test where id = #{id}")
    void executeNoReturnValue(@Param("id") Integer id);


}
