package com.zhengqing.common.base.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * <p> BigDecimal工具类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/8/24 10:45
 */
@Slf4j
public class BigDecimalUtil {

    /**
     * 默认除法运算精度
     */
    private static final int DEF_DIV_SCALE = 2;

    /**
     * 建立货币格式化引用
     */
    private static final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance();

    /**
     * 建立百分比格式化引用
     */
    private static final NumberFormat PERCENT = NumberFormat.getPercentInstance();

    /**
     * 加法
     *
     * @param num1 数1
     * @param num2 数2
     * @return 计算结果
     * @author zhengqingya
     * @date 2021/8/24 10:49
     */
    public static BigDecimal add(BigDecimal num1, BigDecimal num2) {
        return num1.add(num2);
    }


    /**
     * 加法(默认四舍五入，根据scale保留小数位数)
     *
     * @param num1  数1
     * @param num2  数2
     * @param scale 保留小数位数
     * @return 计算结果
     * @author zhengqingya
     * @date 2021/8/24 10:52
     */
    public static BigDecimal add(BigDecimal num1, BigDecimal num2, int scale) {
        return num1.add(num2).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 减法
     *
     * @param num1 数1
     * @param num2 数2
     * @return 计算结果
     * @author zhengqingya
     * @date 2021/8/24 10:54
     */
    public static BigDecimal sub(BigDecimal num1, BigDecimal num2) {
        return num1.subtract(num2);
    }

    /**
     * 减法(默认四舍五入，根据scale保留小数位数)
     *
     * @param num1 数1
     * @param num2 数2
     * @return 计算结果
     * @author zhengqingya
     * @date 2021/8/24 10:54
     */
    public static BigDecimal sub(BigDecimal num1, BigDecimal num2, int scale) {
        return num1.subtract(num2).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 乘法
     *
     * @param num1 数1
     * @param num2 数2
     * @return 计算结果
     * @author zhengqingya
     * @date 2021/8/24 10:54
     */
    public static BigDecimal multiply(BigDecimal num1, BigDecimal num2) {
        return num1.multiply(num2);
    }

    /**
     * 乘法(默认四舍五入，根据scale保留小数位数)
     *
     * @param num1 数1
     * @param num2 数2
     * @return 计算结果
     * @author zhengqingya
     * @date 2021/8/24 10:54
     */
    public static BigDecimal multiply(BigDecimal num1, BigDecimal num2, int scale) {
        return num1.multiply(num2).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 除法(除不尽会抛异常)
     *
     * @param num1 数1
     * @param num2 数2
     * @return 计算结果 = 数1 / 数2
     * @author zhengqingya
     * @date 2021/8/24 10:54
     */
    public static BigDecimal divide(BigDecimal num1, BigDecimal num2) {
        return num1.divide(num2, DEF_DIV_SCALE);
    }

    /**
     * 除法(默认四舍五入保留两位小数)
     *
     * @param num1 数1
     * @param num2 数2
     * @return 计算结果 = 数1 / 数2
     * @author zhengqingya
     * @date 2021/8/24 10:54
     */
    public static BigDecimal divide(BigDecimal num1, BigDecimal num2, int scale) {
        return num1.divide(num2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 比较BigDecimal
     *
     * @param num1 数1
     * @param num2 数2
     * @return 相等返回0, num1>num2返回1, num1<num2返回-1
     * @author zhengqingya
     * @date 2021/8/24 10:54
     */
    public static int compareTo(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2);
    }

    /**
     * BigDecimal货币格式化
     *
     * @param money 钱钱
     * @return java.lang.String
     * @author zhengqingya
     * @date 2021/8/24 11:03
     */
    public static String currencyFormat(BigDecimal money) {
        return CURRENCY.format(money);
    }

    /**
     * BigDecimal百分比格式化
     *
     * @param money 钱钱
     * @return java.lang.String
     * @author zhengqingya
     * @date 2021/8/24 11:03
     */
    public static String rateFormat(BigDecimal money) {
        return PERCENT.format(money);
    }

    /**
     * 保留小数位数
     *
     * @param x     目标小数
     * @param scale 要保留小数位数
     * @return 结果四舍五入
     * @author zhengqingya
     * @date 2021/8/24 11:04
     */
    public static BigDecimal scale(BigDecimal x, int scale) {
        if (x == null) {
            x = BigDecimal.valueOf(0.00);
        }
        return x.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 元转分
     *
     * @param money 钱钱 (如果2位小数以上，自动四舍五入)
     * @return 分
     * @author zhengqingya
     * @date 2021/8/24 11:04
     */
    public static Integer yuanToFen(BigDecimal money) {
        if (money == null) {
            money = BigDecimal.valueOf(0.00);
        }
        return Integer.valueOf(multiply(money, BigDecimal.valueOf(100), 0).toString());
    }

    /**
     * 分转元
     *
     * @param money 钱钱 (如果2位小数以上，自动四舍五入)
     * @return 分
     * @author zhengqingya
     * @date 2021/8/24 11:04
     */
    public static String fenToYuan(String money) {
        if (StringUtils.isBlank(money)) {
            money = "0";
        }
        return String.valueOf(divide(new BigDecimal(money), BigDecimal.valueOf(100), 2));
    }

    /**
     * 分转元
     *
     * @param money 钱钱 (如果2位小数以上，自动四舍五入)
     * @return 分
     * @author zhengqingya
     * @date 2021/8/24 11:04
     */
    public static String fenToYuan(Integer money) {
        if (money == null) {
            money = 0;
        }
        return String.valueOf(divide(BigDecimal.valueOf(money), BigDecimal.valueOf(100), 2));
    }

    /**
     * BigDecimal转Integer
     *
     * @param money 钱钱 (如果有小数，自动四舍五入)
     * @return Integer
     * @author zhengqingya
     * @date 2021/8/24 11:04
     */
    public static Integer toInt(BigDecimal money) {
        if (money == null) {
            return 0;
        }
        return Integer.valueOf(scale(money, 0).toString());
    }

    public static void main(String[] args) {
        BigDecimal num1 = new BigDecimal("80");
        BigDecimal num2 = new BigDecimal("60");

        // 加法
        log.info("加法: {}", BigDecimalUtil.add(num1, num2));
        log.info("加法: {}", BigDecimalUtil.add(num1, num2, 1));

        // 减法
        log.info("减法: {}", BigDecimalUtil.sub(num1, num2));
        log.info("减法: {}", BigDecimalUtil.sub(num1, num2, 2));

        // 乘法
        log.info("乘法: {}", BigDecimalUtil.multiply(num1, num2));
        log.info("乘法: {}", BigDecimalUtil.multiply(num1, num2, 3));

        // 除法
        log.info("除法: {}", BigDecimalUtil.divide(num1, num2));
        log.info("除法: {}", BigDecimalUtil.divide(num1, num2, 4));

        log.info("比较: {}", BigDecimalUtil.compareTo(num1, num2));
        log.info("货币格式化: {}", BigDecimalUtil.currencyFormat(num2));
        log.info("百分比格式化: {}", BigDecimalUtil.rateFormat(num2));
        log.info("元转分: {}", BigDecimalUtil.yuanToFen(num2));
        log.info("分转元: {}", BigDecimalUtil.fenToYuan("0.01"));
        log.info("分转元: {}", BigDecimalUtil.fenToYuan(0));
        log.info("BigDecimal转Integer: {}", BigDecimalUtil.toInt(num2));
    }

}
