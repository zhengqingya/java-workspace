package com.zhengqing.mybatis;

import ognl.Ognl;
import org.junit.Test;

import java.util.HashMap;

/**
 * <p> 测试 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/20 19:22
 */
public class TestOgnl {

    @Test
    public void test() throws Exception {
        // 测试ognl表达式
        Object value = Ognl.getValue("user.age >= 18", new HashMap() {{
            this.put("name", "zhengqingya");
            this.put("user", new HashMap() {{
                this.put("age", 18);
                this.put("name", "zq");
            }});
        }});
        System.out.println(value);
    }

}
