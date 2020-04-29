package com.zhengqing.mybatis.mapper;

import com.zhengqing.mybatis.entity.User;

/**
 * <p> mapper接口 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/4/29 13:54
 */
public interface UserMapper {

  User findOne(Integer id);

}
