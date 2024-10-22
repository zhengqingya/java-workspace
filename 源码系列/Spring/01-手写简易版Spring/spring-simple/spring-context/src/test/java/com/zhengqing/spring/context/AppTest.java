package com.zhengqing.spring.context;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.zhengqing.spring.io.PropertyResolver;
import com.zhengqing.spring.io.Resource;
import com.zhengqing.spring.io.ResourceResolver;
import com.zhengqing.spring.util.ClassUtils;
import com.zhengqing.spring.util.YmlUtil;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AppTest {

    @Test
    public void _01_ResourceResolver() throws Exception {
        ResourceResolver resourceResolver = new ResourceResolver("com.zhengqing");
        List<String> classPathList = resourceResolver.scan(Resource::getPath);
        System.out.println(JSONUtil.toJsonStr(classPathList));
    }

    @Test
    public void _02_yml_load() throws Exception {
        Dict dict = YamlUtil.loadByPath("application.yml");
        System.out.println(dict.getByPath("server.port", Integer.class));
        System.out.println(dict.getByPath("server.shutdown", String.class));
        System.out.println(JSONUtil.toJsonStr(dict));

        Map<String, Object> configMap = YmlUtil.loadAsPlainMap("application.yml");
        System.out.println(JSONUtil.toJsonStr(configMap));
    }

    @Test
    public void _02_yml_load_2() throws Exception {
        Map<String, Object> configMap = YmlUtil.loadAsPlainMap("application.yml");
        System.out.println(configMap.get("server.test.value"));
        System.out.println(JSONUtil.toJsonStr(configMap));
    }

    @Test
    public void _02_PropertyResolver() throws Exception {
        System.out.println("env path: " + System.getenv("Path"));

        Properties props = new Properties();
        props.setProperty("user.name", "zhengqingya");
        PropertyResolver propertyResolver = new PropertyResolver(props);
        System.out.println(propertyResolver.getProperty("user.name"));
        System.out.println(propertyResolver.getProperty("server.port"));
    }

    @Test
    public void _03_BeanDefinition() throws Exception {
//        Method annotationMethod = ClassUtils.findAnnotationMethod(AppTest.class, Test.class);

        System.out.println(ClassUtils.getBeanName(AppTest.class));

    }


}
