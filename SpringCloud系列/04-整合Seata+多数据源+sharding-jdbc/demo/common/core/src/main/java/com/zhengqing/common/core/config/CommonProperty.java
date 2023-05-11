package com.zhengqing.common.core.config;

import lombok.Data;

/**
 * <p>
 * 公共基础配置参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/8/19 9:07
 */
@Data
public class CommonProperty {

    /**
     * ip
     */
    private String ip;

    /**
     * MySQL参数
     */
    private Mysql mysql;

    @Data
    public static class Mysql {
        private MysqlConn master;
        private MysqlConn dbTest;
    }

    @Data
    public static class MysqlConn {
        private String ip;
        private String port;
        private String username;
        private String password;
    }

}
