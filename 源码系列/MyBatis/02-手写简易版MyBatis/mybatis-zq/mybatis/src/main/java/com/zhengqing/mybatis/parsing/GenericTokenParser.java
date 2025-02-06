package com.zhengqing.mybatis.parsing;

import cn.hutool.core.util.StrUtil;

import java.util.List;

/**
 * <p> SQL解析器 </p>
 *
 * @author zhengqingya
 * @description 解析sql： select * from t_user where id = #{id} and name = #{name}
 * ==> select * from t_user where id = ? and name = ?
 * @date 2024/4/22 01:07
 */
public class GenericTokenParser {

    private String openToken; // 开始标记 -- eg: #{
    private String closeToken; // 结束标记 -- eg: }
    private TokenHandler tokenHandler; // 标记处理器

    public GenericTokenParser(String openToken, String closeToken, TokenHandler tokenHandler) {
        this.openToken = openToken;
        this.closeToken = closeToken;
        this.tokenHandler = tokenHandler;
    }

    public String parse(String text) {
        if (StrUtil.isBlank(text)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        char[] textChars = text.toCharArray();
        int offset = 0;
        // select * from t_user where id = #{id} and name = #{name}
        int start = text.indexOf(this.openToken);
        while (start > -1) {
            int end = text.indexOf(this.closeToken, start);
            if (end == -1) {
                result.append(textChars, offset, text.length() - offset);
                offset = text.length();
            } else {
                result.append(textChars, offset, start - offset);
                offset = start + this.openToken.length();
                String paramName = new String(textChars, offset, end - offset);
                result.append(this.tokenHandler.handleToken(paramName));
                offset = end + this.closeToken.length();
            }
            start = text.indexOf(this.openToken, offset);
        }
        if (offset < text.length()) {
            result.append(textChars, offset, text.length() - offset);
        }
        return result.toString();
    }

    public static void main(String[] args) {
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String sql = genericTokenParser.parse("select * from t_user where id = #{id} and name = #{name}");
        System.out.println(sql);

        List<String> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        System.out.println(parameterMappings);

    }


}
