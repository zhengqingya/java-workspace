package com.zhengqing.demo.aviator;

import com.googlecode.aviator.AviatorEvaluator;

/**
 * <p>
 * aviator入门体验
 * </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/8/10 15:41
 */
public class TestAviator {

    public static void main(String[] args) {
        System.out.println("-----------------------------------------------------------------");
        System.out.println("算术表达式【1+1】： " + AviatorEvaluator.execute("1+1"));
        System.out.println("逻辑表达式【1==1】： " + AviatorEvaluator.execute("1==1"));
        System.out.println("三元表达式【1==1 ? '对' : '错'】： " + AviatorEvaluator.execute("1==1 ? '对' : '错'"));
        System.out.println("函数调用【6的3次方】： " + AviatorEvaluator.execute("math.pow(6,3)"));
        System.out.println("-----------------------------------------------------------------");
    }

}
