package com.zhengqing.demo.netty.common.session;

import com.zhengqing.demo.netty.common.session.impl.SessionMemoryImpl;

public abstract class SessionFactory {

    private static Session session = new SessionMemoryImpl();

    public static Session getSession() {
        return session;
    }
}
