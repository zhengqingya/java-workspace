package com.zhengqing.demo.modules.system.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.zhengqing.demo.modules.system.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 *  <p> 系统管理-用户基础信息表 Mapper 接口 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/10/10 11:55
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 列表分页
     *
     * @param page
     * @param filter
     * @return
     */
    List<User> selectUsers(Pagination page, @Param("filter") User filter);

    /**
     * 列表
     *
     * @param filter
     * @return
     */
    List<User> selectUsers(@Param("filter") User filter);
}
