package com.zhengqing.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhengqing.demo.entity.User;

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
     * 列表分页
     *
     * @param params 查询参数
     * @return 查询结果
     * @author zhengqingya
     * @date 2021/01/13 10:11
     */
    IPage<User> listPage(User params);

    /**
     * 新增或更新
     *
     * @param user 保存参数
     * @return 主键id
     * @author zhengqingya
     * @date 2021/01/13 10:11
     */
    Long addOrUpdateData(User user);

    /**
     * 测试插入指定数据用时
     *
     * @param addSum 测试插入数据量
     * @return 耗时
     * @author zhengqingya
     * @date 2021/5/28 14:06
     */
    String addBatchData(int addSum);

}
