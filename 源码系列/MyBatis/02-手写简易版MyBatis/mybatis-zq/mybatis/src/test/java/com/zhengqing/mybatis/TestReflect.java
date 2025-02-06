package com.zhengqing.mybatis;

import cn.hutool.core.util.ReflectUtil;
import lombok.Data;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p> 反射 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/21 19:10
 */
public class TestReflect {

    @Test
    public void test() throws Exception {
        Class<User> userClass = User.class;
        Field nameField = userClass.getField("name"); // public 修饰的字段
        System.out.println(nameField);

        User user = userClass.newInstance(); // 反射创建对象
        nameField.set(user, "zhengqingya");
        System.out.println("nameField: " + nameField.get(user));

        Field ageField = userClass.getDeclaredField("age"); // private 修饰的字段
        System.out.println(ageField);
        ageField.setAccessible(true);
        ageField.set(user, 18);


        Object hello = userClass.getMethod("hello").invoke(user); // public 方法
        System.out.println("hello方法值:" + hello);

        Method eatMethod = userClass.getDeclaredMethod("eat", String.class); // private 私有方法 -- 有参
        eatMethod.setAccessible(true);
        Object eat = eatMethod.invoke(user, "zq");
        System.out.println(eat);

        Method eatMethod2 = userClass.getDeclaredMethod("eat"); // private 私有方法 -- 无参
        eatMethod2.setAccessible(true);
        Object eat2 = eatMethod2.invoke(user);
        System.out.println(eat2);
    }

    @Test
    public void test2() throws Exception {
        System.out.println(ReflectUtil.getField(User.class, "name"));
        Method[] methods = ReflectUtil.getMethods(User.class);
        System.out.println(methods);
    }

    @Data
    static class User {
        public String name;
        private Integer age;

        public String hello() {
            System.out.println("hello...");
            return "hi";
        }

        private String eat() {
            return "吃饭了...";
        }

        private String eat(String name) {
            System.out.println("eat...");
            return name + "吃饭了...";
        }

    }


}
