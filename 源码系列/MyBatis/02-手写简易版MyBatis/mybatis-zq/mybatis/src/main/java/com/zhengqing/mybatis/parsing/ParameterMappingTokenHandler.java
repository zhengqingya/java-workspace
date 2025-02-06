package com.zhengqing.mybatis.parsing;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * <p> 参数处理器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/22 01:44
 */
public class ParameterMappingTokenHandler implements TokenHandler {

    private List<String> parameterMappings = Lists.newArrayList();

    public List<String> getParameterMappings() {
        return this.parameterMappings;
    }

    @Override
    public String handleToken(String content) {
        this.parameterMappings.add(content);
        return "?";
    }
}
