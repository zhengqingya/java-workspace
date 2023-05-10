package com.zhengqing.common.base.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * <p>
 * 自增版本号工具类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/10/13 14:08
 */
public class AutoUpgradeVersionUtil {

    /**
     * 自动自增版本号 ex:0.0.1 -> 0.0.2
     *
     * @param version 版本号
     * @return 自增后的版本号
     * @author zhengqingya
     * @date 2020/10/13 14:13
     */
    public static String autoUpgradeVersion(String version) {
        if (StringUtils.isBlank(version)) {
            version = "0.0.1";
        }
        // 将版本号拆解成整数数组
        String[] versionArray = version.split("\\.");
        int[] versionList = Arrays.stream(versionArray).mapToInt(Integer::valueOf).toArray();

        // 递归调用
        autoUpgradeVersion(versionList, versionList.length - 1);

        // 数组转字符串
        version = StringUtils.join(versionList, '.');
        return version;
    }

    private static void autoUpgradeVersion(int[] ints, int index) {
        if (index == 0) {
            ints[0] = ints[0] + 1;
        } else {
            int value = ints[index] + 1;
            if (value < 10) {
                ints[index] = value;
            } else {
                ints[index] = 0;
                autoUpgradeVersion(ints, index - 1);
            }
        }
    }

}
