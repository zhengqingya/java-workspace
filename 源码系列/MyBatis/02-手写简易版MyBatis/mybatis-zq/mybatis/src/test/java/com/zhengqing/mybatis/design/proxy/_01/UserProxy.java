package com.zhengqing.mybatis.design.proxy._01;

/**
 * <p> 用户代理 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/21 17:10
 */
public class UserProxy implements UserService {

    private UserService userService;

    public UserProxy(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object selectList(String name) {
        System.out.println("代理类执行了 start...");
        Object list = this.userService.selectList(name);
        System.out.println("代理类执行了 end...");
        return list;
    }

    @Override
    public Object selectOne(String name) {
        System.out.println("代理类执行了 start...");
        Object one = this.userService.selectOne(name);
        System.out.println("代理类执行了 end...");
        return one;
    }
    
}
