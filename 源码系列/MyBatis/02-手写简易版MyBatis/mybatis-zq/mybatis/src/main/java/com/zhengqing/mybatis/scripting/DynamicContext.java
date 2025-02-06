package com.zhengqing.mybatis.scripting;

import java.util.Map;
import java.util.StringJoiner;

/**
 * <p> 静态上下文 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/29 04:26
 */
public class DynamicContext {

    private Map<String, Object> bindings;
    private StringJoiner sqlBuilder = new StringJoiner(" ");

    public DynamicContext(Map<String, Object> bindings) {
        this.bindings = bindings;
    }

    public Map<String, Object> getBindings() {
        return this.bindings;
    }

    public void appendSql(String sqlText) {
        this.sqlBuilder.add(sqlText.trim());
    }

    public String getSql() {
        return this.sqlBuilder.toString();
    }
}
