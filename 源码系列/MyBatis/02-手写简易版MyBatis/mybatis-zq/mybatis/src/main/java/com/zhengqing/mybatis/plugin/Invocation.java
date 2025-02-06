package com.zhengqing.mybatis.plugin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

/**
 * <p> 代理对象执行所需参数 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/26 01:31
 */
@Data
@AllArgsConstructor
public class Invocation {

    private Object target; // 代理对象
    private Method method; // 执行方法
    private Object[] args; // 方法参数

    @SneakyThrows
    public Object proceed() {
        return this.method.invoke(this.target, this.args);
    }

}
