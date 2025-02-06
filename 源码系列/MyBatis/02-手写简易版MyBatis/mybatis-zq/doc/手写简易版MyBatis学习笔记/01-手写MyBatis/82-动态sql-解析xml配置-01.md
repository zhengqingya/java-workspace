# 动态sql-解析xml配置-01

### 简单解析测试

```java
public interface UserMapper {
    User findOne(Integer id);
}
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.zhengqing.demo.mapper.UserMapper">
    <select id="findOne" resultType="com.zhengqing.demo.entity.User">
        select * from t_user where id = #{id}
        <if test="id != null">
            and id = #{id}
            <if test="xx != null">
                limit 1
            </if>
        </if>
    </select>
</mapper>
```

单层解析

```java
import cn.hutool.core.io.FileUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class TestXml {
    @Test
    public void test() throws Exception {
        // 解析xml
        SAXReader saxReader = new SAXReader();
        saxReader.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
            }
        });  // 跳过 xml DTD 验证 -- 解决解析慢的问题
        BufferedInputStream inputStream = FileUtil.getInputStream("/Users/zhengqingya/zhengqingya/code/workspace-me/mybatis-zq/src/main/java/com/zhengqing/demo/mapper/UserMapper.xml");
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.selectNodes("//select");
        for (Element selectElement : list) {
            String methodName = selectElement.attributeValue("id");
            String resultType = selectElement.attributeValue("resultType");
            List<Node> contentList = selectElement.content();
            for (Node node : contentList) {
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element childNodeElement = (Element) node;
                    String sqlNodeType = childNodeElement.getName();
                    String test = childNodeElement.attributeValue("test");
                    System.out.println("类型：" + sqlNodeType);
                    System.out.println("表达式：" + test);
                } else {
                    String sql = node.getText();
                    System.out.println("sql：" + sql);
                }
            }
        }
    }
}
```

递归解析

```java
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class TestXml {

    @Test
    public void test() throws Exception {
        // 解析xml
        SAXReader saxReader = new SAXReader();
        saxReader.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
            }
        });  // 跳过 xml DTD 验证 -- 解决解析慢的问题
        BufferedInputStream inputStream = FileUtil.getInputStream("/Users/zhengqingya/zhengqingya/code/workspace-me/mybatis-zq/src/main/java/com/zhengqing/demo/mapper/UserMapper.xml");
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.selectNodes("//select");
        for (Element selectElement : list) {
            String methodName = selectElement.attributeValue("id");
            String resultType = selectElement.attributeValue("resultType");
            List<Node> contentList = selectElement.content();
            for (Node node : contentList) {
                this.parseTags(node);
            }
        }
    }

    private void parseTags(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element childNodeElement = (Element) node;
            String sqlNodeType = childNodeElement.getName();
            String test = childNodeElement.attributeValue("test");
            System.out.println("类型：" + sqlNodeType);
            System.out.println("表达式：" + test);
            List<Node> contentList = childNodeElement.content();
            contentList.forEach(item -> this.parseTags(item));
        } else {
            String sql = node.getText();
            if (StrUtil.isNotBlank(sql)) {
                System.out.println("sql：" + sql.trim());
            }
        }
    }
}
```