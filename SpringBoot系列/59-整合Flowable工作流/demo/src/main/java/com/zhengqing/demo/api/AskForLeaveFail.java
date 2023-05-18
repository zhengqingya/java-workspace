package com.zhengqing.demo.api;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class AskForLeaveFail implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("请假失败。。。");
    }
}
