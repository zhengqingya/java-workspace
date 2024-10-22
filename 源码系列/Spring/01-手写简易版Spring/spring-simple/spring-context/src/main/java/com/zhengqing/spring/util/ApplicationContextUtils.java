package com.zhengqing.spring.util;

import com.zhengqing.spring.context.ApplicationContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class ApplicationContextUtils {

    private static ApplicationContext applicationContext = null;

    @Nonnull
    public static ApplicationContext getRequiredApplicationContext() {
        return Objects.requireNonNull(getApplicationContext(), "ApplicationContext is not set.");
    }

    @Nullable
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext ctx) {
        applicationContext = ctx;
    }
}
