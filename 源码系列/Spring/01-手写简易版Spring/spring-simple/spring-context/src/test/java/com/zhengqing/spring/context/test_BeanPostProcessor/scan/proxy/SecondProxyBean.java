package com.zhengqing.spring.context.test_BeanPostProcessor.scan.proxy;

// 代理Bean
public class SecondProxyBean extends OriginBean {
    final OriginBean target;

    public SecondProxyBean(OriginBean target) {
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