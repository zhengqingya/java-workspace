package com.zhengqing.mytest.util;

import cn.hutool.core.util.StrUtil;
import com.zhengqing.mytest.config.BaseProperty;

public class TestUtil {

    private BaseProperty baseProperty;

    public TestUtil(BaseProperty baseProperty) {
        this.baseProperty = baseProperty;
    }

    public void hello() {
        System.err.println(
                StrUtil.format("工具类：{} {}",
                        this.baseProperty.getIp(),
                        this.baseProperty.getName()
                )
        );
    }

}
