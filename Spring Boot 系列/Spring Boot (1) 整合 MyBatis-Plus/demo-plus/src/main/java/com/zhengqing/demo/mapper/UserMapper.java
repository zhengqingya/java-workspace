package com.zhengqing.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhengqing.demo.entity.User;
import org.apache.ibatis.annotations.MapKey;

import java.util.Map;


/**
 * <p> 用户 Mapper 接口 </p>
 *
 * @author zhengqing
 * @description
 * @date 2019/10/10 11:55
 */
public interface UserMapper extends BaseMapper<User> {

    @MapKey("id")
    Map<Integer, User> selectMap();


}
