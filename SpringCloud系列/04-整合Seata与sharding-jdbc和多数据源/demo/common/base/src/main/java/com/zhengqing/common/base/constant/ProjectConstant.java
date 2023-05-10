package com.zhengqing.common.base.constant;

/**
 * <p> 全局常用变量 - 工程使用 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/7/20 18:16
 */
public interface ProjectConstant {

    // ===============================================================================
    // ============================ ↓↓↓↓↓↓ 文件系列 ↓↓↓↓↓↓ ============================
    // ===============================================================================

    /**
     * 系统分隔符
     */
    String SYSTEM_SEPARATOR = "/";

    /**
     * 获取项目根目录
     */
    String PROJECT_ROOT_DIRECTORY = System.getProperty("user.dir").replaceAll("\\\\", SYSTEM_SEPARATOR);

    /**
     * 临时文件相关
     */
    String DEFAULT_FOLDER_TMP = PROJECT_ROOT_DIRECTORY + "/tmp";
    String DEFAULT_FOLDER_TMP_GENERATE = PROJECT_ROOT_DIRECTORY + "/tmp-generate";

    // ===============================================================================
    // ============================ ↓↓↓↓↓↓ other ↓↓↓↓↓↓ ==============================
    // ===============================================================================

    /**
     * 实体类
     */
    String ENTITY_PACKAGE = "com.zhengqing.*.entity";
    /**
     * mapper
     */
    String MAPPER_PACKAGE = "com.zhengqing.*.mapper";

}
