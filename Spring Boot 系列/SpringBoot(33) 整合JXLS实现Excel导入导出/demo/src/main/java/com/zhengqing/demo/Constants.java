package com.zhengqing.demo;

/**
 * <p>
 * 全局常用变量
 * </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/4/21 23:34
 */
public class Constants {

    /**
     * 导入导出文件相关
     */
    public static String DEFAULT_REPORT_IMPORT_FOLDER = "/report/import";
    public static String DEFAULT_REPORT_EXPORT_FOLDER = "/report/export";

    /**
     * 系统分隔符
     */
    public static String SYSTEM_SEPARATOR = "/";

    /**
     * 获取项目根目录
     */
    public static String PROJECT_ROOT_DIRECTORY = System.getProperty("user.dir").replaceAll("\\\\", SYSTEM_SEPARATOR);

    /**
     * excel导出测试临时存储路径
     */
    public static String FILE_PATH_TEST_EXPORT_EXCEL = PROJECT_ROOT_DIRECTORY + "/excel.xls";

}
