package com.zhengqing.common.base.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * <p>
 * 业务断言
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/5/21 20:27
 */
public final class MyAssert extends Assert {

    /**
     * 断言对象不为空
     *
     * @param object 对象
     * @param msg    不满足断言的异常信息
     */
    public static void notNull(Object object, String msg) {
        state(object != null, msg);
    }

    public static void notNull(Object object, Supplier<String> supplier) {
        state(object != null, supplier);
    }

    /**
     * 断言字符串不为空
     *
     * @param str 字符串
     * @param msg 不满足断言的异常信息
     */
    public static void notEmpty(String str, String msg) {
        state(!StringUtils.isEmpty(str), msg);
    }

    /**
     * 断言集合不为空
     *
     * @param collection 集合
     * @param msg        不满足断言的异常信息
     */
    public static void notEmpty(Collection<?> collection, String msg) {
        state(!CollectionUtils.isEmpty(collection), msg);
    }

    /**
     * 断言一个boolean表达式
     *
     * @param expression boolean表达式
     * @param message    不满足断言的异常信息
     */
    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new MyException(message);
        }
    }

    /**
     * 断言一个boolean表达式，用于需要大量拼接字符串以及一些其他操作等
     *
     * @param expression boolean表达式
     * @param supplier   msg生产者
     */
    public static void state(boolean expression, Supplier<String> supplier) {
        if (!expression) {
            throw new MyException(supplier.get());
        }
    }

}
