package com.zhengqing.demo.netty.common.session;

import com.zhengqing.demo.netty.common.session.impl.GroupSessionMemoryImpl;

public abstract class GroupSessionFactory {

    private static GroupSession session = new GroupSessionMemoryImpl();

    public static GroupSession getGroupSession() {
        return session;
    }
}
