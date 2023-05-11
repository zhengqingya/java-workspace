package com.zhengqing.common.log.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

/**
 * <p> 日志级别工具类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/2/1 12:12
 */
public class LogLevelUtil {

    /**
     * 修改日志级别
     *
     * @param packageName 包名
     * @param logLevel    日志级别
     * @return void
     * @author zhengqingya
     * @date 2021/2/1 12:12
     */
    public static void update(String packageName, Level logLevel) {
        // 获取LoggerContext实例
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        // 获取RootLogger并设置日志级别
        loggerContext.getLogger("ROOT").setLevel(Level.DEBUG);
        // 获取指定包下的Logger并设置日志级别
        loggerContext.getLogger(packageName).setLevel(logLevel);
    }

}
