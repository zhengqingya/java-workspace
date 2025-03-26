package com.zhengqing.demo;

import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 通过反射执行指定包下的类以及内部类的方法，
 * 如方法参数为基本数据类型、Date、BigDecimal等则设置默认值进行覆盖（枚举遍历输出枚举值）
 * </p>
 *
 * @author zhengqingya
 * @description 见 https://juejin.cn/post/7404130389985787930#heading-5
 * @date 2024/11/7 9:46
 */
@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class PojoCoverTest {

    /**
     * 需要覆盖的包集合
     */
    private final static List<String> SCAN_PACKAGE_LIST = Lists.newArrayList(
            "com.zhengqing.demo.service"
    );

    /**
     * 类加载器
     */
    private ClassLoader classLoader = null;

    @Before
    public void before() {
        // 获取当前类加载器
        classLoader = Thread.currentThread().getContextClassLoader();
    }

    /**
     * 反射执行所有：pojo、enum、exlpame 等基础domain类。
     */
    @Test
    public void domainCoverTest() {
        // 获取class loader
        for (String packageName : SCAN_PACKAGE_LIST) {
            try {
                // 加载指定包以及子包的类
                ClassPath classPath = ClassPath.from(classLoader);
                Set<ClassPath.ClassInfo> classInfos = classPath.getTopLevelClassesRecursive(packageName);
                log.info(">>>>>>> domainCoverTest, packageName:{}, classSize:{}", packageName, classInfos.size());
                // 覆盖单测
                for (ClassPath.ClassInfo classInfo : classInfos) {
                    this.coverDomain(classInfo.load());
                }
            } catch (Throwable e) {
                log.error(">>>>>>> domainCoverTest Exception package:{}", packageName, e);
            }
        }
    }

    private void coverDomain(Class<?> clazz) {
        boolean canInstance = this.canInstance(clazz);
        if (!canInstance) {
            return;
        }

        // 枚举，执行所有值
        if (clazz.isEnum()) {
            Object[] enumList = clazz.getEnumConstants();
            for (Object enumField : enumList) {
                // 输出每一行枚举值
                String enumString = enumField.toString();
            }
        }

        // 执行外部类的所有方法
        Object outerInstance = null;
        try {
            outerInstance = clazz.getDeclaredConstructor().newInstance();
            this.method(clazz, outerInstance);
        } catch (Throwable ignored) {
        }

        // 执行指定内部类的方法
        for (Class<?> innerClass : clazz.getDeclaredClasses()) {
            try {
                boolean innerCanInstance = this.canInstance(clazz);
                if (!innerCanInstance) {
                    continue;
                }
                boolean isStatic = Modifier.isStatic(innerClass.getModifiers());
                Object innerClazzInstance = null;
                if (isStatic) {
                    Constructor<?> constructor = innerClass.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    innerClazzInstance = constructor.newInstance();
                } else {
                    Constructor<?> constructor = innerClass.getDeclaredConstructor(clazz);
                    constructor.setAccessible(true);
                    innerClazzInstance = constructor.newInstance(outerInstance);
                }
                this.method(innerClass, innerClazzInstance);
            } catch (Throwable ignored) {
            }
        }
    }

    private boolean canInstance(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        boolean isAnnotation = clazz.isAnnotation();
        boolean isInterface = clazz.isInterface();
        boolean isEnum = clazz.isEnum();
        boolean isAbstract = Modifier.isAbstract(modifiers);
        boolean isNative = Modifier.isNative(modifiers);
        log.info(">>>>>>> coverDomain class:{}, isAnnotation:{}, isInterface:{}, isEnum:{}, isAbstract:{}, isNative:{}", clazz.getName(), isAnnotation, isInterface, isEnum, isAbstract, isNative);
        if (isAnnotation || isInterface || isAbstract || isNative) {
            return false;
        }
        // 如果是静态类或者final类，且不是枚举类也不处理
        return isEnum || (!Modifier.isFinal(modifiers));
    }

    /**
     * 通过反射调用指定实例的方法
     *
     * @param clazz    方法所属的类对象
     * @param instance 方法所属的实例对象
     */
    private void method(Class<?> clazz, Object instance) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (!Modifier.isStatic(method.getModifiers())) {
                method.setAccessible(true);
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            try {
                if (parameterTypes.length == 0) {
                    method.invoke(instance);
                } else {
                    // null 值覆盖
                    try {
                        Object[] parameters = new Object[parameterTypes.length];
                        for (int i = 0; i < parameterTypes.length; i++) {
                            Class<?> paramType = parameterTypes[i];
                            parameters[i] = this.getValue(paramType, true);
                        }
                        method.invoke(instance, parameters);
                    } catch (Throwable ignore) {
                    }
                    // 非 null 值覆盖
                    try {
                        Object[] parameters = new Object[parameterTypes.length];
                        for (int i = 0; i < parameterTypes.length; i++) {
                            Class<?> paramType = parameterTypes[i];
                            parameters[i] = this.getValue(paramType, false);
                        }
                        method.invoke(instance, parameters);
                    } catch (Throwable ignore) {
                    }
                }
            } catch (Throwable ignored) {
            }
        }
    }

    /**
     * 通过类的type 返回对应的默认值， 如果有其他类型请大家自行补充
     *
     * @param type 入参字段类型
     * @return 返回对应字段的默认值
     */
    private Object getValue(Class<?> type, boolean useNull) {
        if (type.isPrimitive()) {
            if (type.equals(boolean.class)) {
                return false;
            } else if (type.equals(char.class)) {
                return '\0';
            } else if (type.equals(byte.class)) {
                return (byte) 0;
            } else if (type.equals(short.class)) {
                return (short) 0;
            } else if (type.equals(int.class)) {
                return 0;
            } else if (type.equals(long.class)) {
                return 0L;
            } else if (type.equals(float.class)) {
                return 0F;
            } else if (type.equals(double.class)) {
                return 0.0;
            }
        }
        if (useNull) {
            return null;
        }
        if (type.equals(String.class)) {
            return "1";
        } else if (type.equals(Integer.class)) {
            return 1;
        } else if (type.equals(Long.class)) {
            return 1L;
        } else if (type.equals(Double.class)) {
            return 1.1D;
        } else if (type.equals(Float.class)) {
            return 1.1F;
        } else if (type.equals(Byte.class)) {
            return Byte.valueOf("1");
        } else if (type.equals(List.class)) {
            return new ArrayList<>();
        } else if (type.equals(Short.class)) {
            return Short.valueOf("1");
        } else if (type.equals(Date.class)) {
            return new Date();
        } else if (type.equals(Boolean.class)) {
            return true;
        } else if (type.equals(BigDecimal.class)) {
            return BigDecimal.ONE;
        } else {
            // 对于非原始类型和String，我们不提供默认值，即不传递参数
            return null;
        }
    }
}
