package com.zhengqing.spring.io;

import cn.hutool.core.util.ClassUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * <p> 扫描指定包下的所有Class </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/10/10 15:51
 */
@Slf4j
public class ResourceResolver {
    String basePackage;

    public ResourceResolver(String basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * 仅扫描class，不扫描接口、枚举、注解
     */
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
