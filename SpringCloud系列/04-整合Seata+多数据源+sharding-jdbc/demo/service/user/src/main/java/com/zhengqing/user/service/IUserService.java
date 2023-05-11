package com.zhengqing.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhengqing.user.entity.User;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
public interface IUserService extends IService<User> {

    /**
     * 详情
     *
     * @param userId 用户ID
     * @return 用户信息
     * @author zhengqingya
     * @date 2021/11/14 9:32 下午
     */
    User detail(Long userId);

    /**
     * 测试分布式事务
     *
     * @return void
     * @author zhengqingya
     * @date 2021/11/16 9:28 下午
     */
    void testSeata();

}
