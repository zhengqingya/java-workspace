package com.zhengqing.mybatis.scripting;

import lombok.SneakyThrows;

import java.util.List;

/**
 * <p> 混合 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/29 02:41
 */
public class MixedSqlNode implements SqlNode {

    private List<SqlNode> contents; // eg: IfSqlNode, xx

    public MixedSqlNode(List<SqlNode> contents) {
        this.contents = contents;
    }

    @SneakyThrows
    @Override
    public void apply(DynamicContext context) {
        for (SqlNode content : this.contents) {
            content.apply(context);
        }
    }


}
