# 实现PropertyResolver

目的：实现`PropertyResolver`，用于读取配置。

---

Spring的注入分为`@Autowired`和`@Value`两种。

- `@Autowired`：Bean的依赖
- `@Value`：注入配置，不涉及Bean的依赖

为了注入配置，我们用`PropertyResolver`保存所有配置项，对外提供查询功能。

- 根据配置的key查询，eg：`getProperty("server.port")`;

Java本身提供了按key-value查询的`Properties`，我们先传入`Properties`，内部按key-value存储：

```java
public class PropertyResolver {
    // 存储所有配置项
    Map<String, String> properties = new HashMap<>();

    public PropertyResolver(Properties props) {
        // 存入环境变量
        this.properties.putAll(System.getenv());
        // 存入Properties
        Set<String> names = props.stringPropertyNames();
        for (String name : names) {
            this.properties.put(name, props.getProperty(name));
        }
    }
    
    /**
     * 获取配置项
     */
    public Object getProperty(String key) {
        return this.properties.get(key);
    }
}
```


### YAML配置读取

```
<!-- yml配置读取 -->
<!-- https://mvnrepository.com/artifact/org.yaml/snakeyaml -->
<dependency>
    <groupId>org.yaml</groupId>
    <artifactId>snakeyaml</artifactId>
    <version>2.3</version>
</dependency>
```

```yaml
server:
  port: 80
  shutdown: GRACEFUL

name: 你好呀！
```

```java
public class YmlUtil {
    public static Map<String, Object> loadAsPlainMap(String path) {
        Map<String, Object> configMap = Maps.newHashMap();
        if (StrUtil.isBlank(path)) {
            return configMap;
        }
        Map<String, Object> source = YamlUtil.loadByPath(path, Map.class);
        return loadAsPlainMap(configMap, source, null);
    }

    private static Map<String, Object> loadAsPlainMap(Map<String, Object> configMap, Map<String, Object> source, String prefix) {
        source.forEach((key, val) -> {
            if (val instanceof Map) {
                loadAsPlainMap(configMap, (Map) val, key + ".");
            } else {
                configMap.put(StrUtil.isBlank(prefix) ? key : prefix + key, val);
            }
        });
        return configMap;
    }
}
```

```java
@Test
public void _02_yml_load() throws Exception {
    Dict dict = YamlUtil.loadByPath("application.yml");
    System.out.println(dict.getByPath("server.port", Integer.class)); // 80
    System.out.println(dict.getByPath("server.shutdown", String.class)); // GRACEFUL
    System.out.println(JSONUtil.toJsonStr(dict)); // {"server":{"port":80,"shutdown":"GRACEFUL"},"name":"你好呀！"}

    Map<String, Object> configMap = YmlUtil.loadAsPlainMap("application.yml");
    System.out.println(JSONUtil.toJsonStr(configMap)); // {"server.port":80,"name":"你好呀！","server.shutdown":"GRACEFUL"}
}
```

最终 `PropertyResolver`

```java
@Slf4j
public class PropertyResolver {

    // 存储所有配置项
    Map<String, Object> properties = new HashMap<>();

    public PropertyResolver(Properties props) {
        // 存入环境变量
        this.properties.putAll(System.getenv());

        // 存入Properties
        Set<String> names = props.stringPropertyNames();
        for (String name : names) {
            this.properties.put(name, props.getProperty(name));
        }

        // yml配置读取
        this.properties.putAll(YmlUtil.loadAsPlainMap("application.yml"));

        if (log.isDebugEnabled()) {
            List<String> keys = new ArrayList<>(this.properties.keySet());
            Collections.sort(keys);
            for (String key : keys) {
                log.debug("PropertyResolver: {} = {}", key, this.properties.get(key));
            }
        }
    }

    /**
     * 获取配置项
     */
    public Object getProperty(String key) {
        return this.properties.get(key);
    }

}
```

测试

```java
@Test
public void _02_PropertyResolver() throws Exception {
    System.out.println("env path: " + System.getenv("Path"));

    Properties props = new Properties();
    props.setProperty("user.name", "zhengqingya");
    PropertyResolver propertyResolver = new PropertyResolver(props);
    System.out.println(propertyResolver.getProperty("user.name"));
    System.out.println(propertyResolver.getProperty("server.port"));
}
```
