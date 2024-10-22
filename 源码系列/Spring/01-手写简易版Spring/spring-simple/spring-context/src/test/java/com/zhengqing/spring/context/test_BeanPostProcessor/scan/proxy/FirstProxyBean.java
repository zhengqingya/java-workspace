package com.zhengqing.spring.context.test_BeanPostProcessor.scan.proxy;

public class FirstProxyBean extends OriginBean {

    final OriginBean target;

    public FirstProxyBean(OriginBean target) {
        this.target = target;
    }

    @Override
    public void setPassword(String password) {
        target.setPassword(password);
    }

    @Override
    public String getUsername() {
        return target.getUsername();
    }

    @Override
    public String getPassword() {
        return target.getPassword();
    }
}