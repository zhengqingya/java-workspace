# SQL解析器

解析sql：

- select * from t_user where id = #{id} and name = #{name}
- ==> select * from t_user where id = ? and name = ?

```java
import cn.hutool.core.util.StrUtil;

public class GenericTokenParser {

    private String openToken; // 开始标记 -- eg: #{
    private String closeToken; // 结束标记 -- eg: }

    public GenericTokenParser(String openToken, String closeToken) {
        this.openToken = openToken;
        this.closeToken = closeToken;
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
                System.out.println(paramName);
                result.append("?");
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
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}");
        String sql = genericTokenParser.parse("select * from t_user where id = #{id} and name = #{name}");
        System.out.println(sql);
    }

}
```