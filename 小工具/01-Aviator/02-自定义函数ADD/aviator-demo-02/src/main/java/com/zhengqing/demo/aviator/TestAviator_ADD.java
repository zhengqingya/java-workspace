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
 * @description : 可参考：https://www.yuque.com/boyan-avfmj/aviatorscript/xbdgg2
 * @date : 2020/8/10 11:41
 */
public class TestAviator_ADD {

    public static void main(String[] args) {
        // 注册函数`ADD`
        AviatorEvaluator.addFunction(new Function_ADD());
        Object execute1 = AviatorEvaluator.execute("ADD(10.5, 20)");
        Object execute2 = AviatorEvaluator.execute("ADD(ADD(1, 2), 100)");
        System.out.println(execute1);
        System.out.println(execute2);
    }

}

/**
 * <p>
 * 自定义函数`ADD`
 * </p>
 *
 * @author : zhengqing
 * @description : 继承`com.googlecode.aviator.runtime.function.AbstractFunction`,并覆写对应参数个数的方法即可
 * @date : 2020/8/10 21:30
 */
class Function_ADD extends AbstractFunction {

    /**
     * 实现将两个参数相加，返回浮点结果 AviatorDouble
     *
     * @param env:当前执行的上下文
     * @param arg1:
     *            左参数
     * @param arg2:
     *            右参数
     * @return: com.googlecode.aviator.runtime.type.AviatorObject
     * @date : 2020/8/10 21:33
     */
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        Number left = FunctionUtils.getNumberValue(arg1, env);
        Number right = FunctionUtils.getNumberValue(arg2, env);
        return new AviatorDouble(left.doubleValue() + right.doubleValue());
    }

    /**
     * 返回方法名
     */
    @Override
    public String getName() {
        return "ADD";
    }

}
