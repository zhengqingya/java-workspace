package com.zhengqing.common.db.constant;

/**
 * <p> 全局常用变量 - mybatis </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/6/1 17:08
 */
public interface MybatisConstant {

    /**
     * mybatis 分页参数
     */
    String LIMIT_ONE = "LIMIT 1";
    /**
     * 是否删除：true->删除，false->未删除
     */
    String IS_DELETED = "isDeleted";
    /**
     * 创建人id
     */
    String CREATE_BY = "createBy";
    /**
     * 创建时间
     */
    String CREATE_TIME = "createTime";
    /**
     * 更新人id
     */
    String UPDATE_BY = "updateBy";
    /**
     * 更新时间
     */
    String UPDATE_TIME = "updateTime";

}
