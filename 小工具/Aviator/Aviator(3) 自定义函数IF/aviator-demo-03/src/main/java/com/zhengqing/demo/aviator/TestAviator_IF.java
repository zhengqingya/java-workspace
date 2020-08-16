package com.zhengqing.demo.aviator;

import java.util.Map;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

/**
 * <p>
 * Aviator 自定义函数和调用 Java 方法
 * </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/8/11 9:16
 */
public class TestAviator_IF {

    public static void main(String[] args) {
        // 注册函数`IF`
        AviatorEvaluator.addFunction(new Function_IF());
        Object execute1 = AviatorEvaluator.execute(" IF(1.0==1.0,100,200)");
        Object execute2 = AviatorEvaluator.execute("IF(1==1,IF(1>=2,10,20),IF(10<=20,30,40))");
        System.out.println(execute1);
        System.out.println(execute2);
    }

}

/**
 * <p>
 * Aviator 自定义函数`IF`
 * </p>
 *
 * @author : zhengqing
 * @description : 继承`com.googlecode.aviator.runtime.function.AbstractFunction`,并覆写对应参数个数的方法即可
 * @date : 2020/8/11 9:15
 */
class Function_IF extends AbstractFunction {

    /**
     * IF函数实现逻辑处理
     *
     * @param env:
     *            当前执行的上下文
     * @param arg1:
     *            if函数中的判断逻辑
     * @param arg2:
     *            arg1为true时的结果
     * @param arg3:
     *            arg1为false时的结果
     * @return: com.googlecode.aviator.runtime.type.AviatorObject
     * @author : zhengqing
     * @date : 2020/8/11 9:17
     */
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        Boolean ifResult = FunctionUtils.getBooleanValue(arg1, env);
        Number ifTrue = FunctionUtils.getNumberValue(arg2, env);
        Number ifFalse = FunctionUtils.getNumberValue(arg3, env);
        if (ifResult) {
            return new AviatorDouble(ifTrue.doubleValue());
        } else {
            return new AviatorDouble(ifFalse.doubleValue());
        }
    }

    /**
     * 返回方法名
     */
    @Override
    public String getName() {
        return "IF";
    }

}
