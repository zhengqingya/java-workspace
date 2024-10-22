# 实现ResourceResolver

实现`@ComponentScan`：扫描指定包下的所有Class

---

我们先定义一个`Resource`记录class信息

```java
@Data
@AllArgsConstructor
public class Resource {
    String name;
    String path;
}
```

再仿造Spring提供一个`ResourceResolver`，定义`scan()`方法来获取扫描到的`Resource`：

```java
@Slf4j
public class ResourceResolver {
    String basePackage;

    public ResourceResolver(String basePackage) {
        this.basePackage = basePackage;
    }

    public <R> List<R> scan(Function<Resource, R> mapper) {
        List<R> collector = Lists.newArrayList();
        Set<Class<?>> classes = ClassUtil.scanPackage(basePackage);
        classes.forEach(item -> {
            String path = item.getName();
            String[] split = path.split("\\.");
            String name = split[split.length - 1];
            Resource resource = new Resource(name, path);
            R apply = mapper.apply(resource);
            if (apply != null) {
                collector.add(apply);
            }
        });
        return collector;
    }
}
```

测试

```java
@Test
public void _01_ResourceResolver() throws Exception {
    ResourceResolver resourceResolver = new ResourceResolver("com.zhengqing");
    List<String> classPathList = resourceResolver.scan(Resource::getPath);
    System.out.println(JSONUtil.toJsonStr(classPathList));
}
```