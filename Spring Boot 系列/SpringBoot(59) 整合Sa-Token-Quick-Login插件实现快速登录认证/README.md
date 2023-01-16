# Sa-Token-Quick-Login插件实现快速登录认证

https://sa-token.cc/doc.html#/plugin/quick-login

### 整合

#### 1、pom.xml

```
<!-- Sa-Token 权限认证，在线文档：https://sa-token.cc -->
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-spring-boot-starter</artifactId>
    <version>1.34.0</version>
</dependency>
<!-- Sa-Token-Quick-Login 插件 -->
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-quick-login</artifactId>
    <version>1.34.0</version>
</dependency>
```

#### 2、application.yml

```yml
# Sa-Token-Quick-Login 配置
sa:
  # 登录账号
  name: test
  # 登录密码
  pwd: 123456
  # 是否自动随机生成账号密码 (此项为true时, name与pwd失效)
  auto: false
  # 是否开启全局认证(关闭后将不再强行拦截)
  auth: true
  # 登录页标题
  title: 登录
  # 是否显示底部版权信息
  copr: true
  # 指定拦截路径
  include: /**
  # 指定排除路径
  exclude: /1.jpg
```

#### 3、测试

```java

@RestController
public class TestController {
    @RequestMapping({"/", "/index"})
    public String index() {
        String str = "<br />"
                + "<h1 style='text-align: center;'>资源页 （登录后才可进入本页面） </h1>"
                + "<hr/>"
                + "<p style='text-align: center;'> zhengqingya tips </p>";
        return str;
    }
}
```

浏览器访问测试： http://localhost:666
