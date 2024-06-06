package com.zhengqing.demo.constant;

/**
 * <p>
 * 全局常用变量
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/10/12 14:47
 */
public interface AppConstant {

    // ===============================================================================
    // ============================ ↓↓↓↓↓↓ 文件系列 ↓↓↓↓↓↓ ============================
    // ===============================================================================

    /**
     * 系统分隔符
     */
    String SEPARATOR_SPRIT = "/";
    /**
     * 分隔符 - 逗号
     */
    String SEPARATOR_COMMA = ",";
    /**
     * 获取项目根目录
     */
    String PROJECT_ROOT_DIRECTORY = System.getProperty("user.dir")
            .replaceAll("\\\\", SEPARATOR_SPRIT);

    /**
     * 读写根目录
     */
    String READ_ROOT_DIRECTORY = PROJECT_ROOT_DIRECTORY + "/doc/read/";
    String WRITE_ROOT_DIRECTORY = PROJECT_ROOT_DIRECTORY + "/doc/write/";

}
