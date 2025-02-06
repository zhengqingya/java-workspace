package com.zhengqing.mybatis.scripting;

import lombok.SneakyThrows;
import ognl.Ognl;

/**
 * <p> IF </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/29 02:41
 */
public class IfSqlNode implements SqlNode {

    private String test; // eg: id != null

    private SqlNode sqlNode; // eg: IfSqlNode

    public IfSqlNode(String test, SqlNode sqlNode) {
        this.test = test;
        this.sqlNode = sqlNode;
    }

    @SneakyThrows
    @Override
    public void apply(DynamicContext context) {
        Boolean value = (Boolean) Ognl.getValue(this.test, context.getBindings());
        if (value) {
            this.sqlNode.apply(context);
        }
    }


}
