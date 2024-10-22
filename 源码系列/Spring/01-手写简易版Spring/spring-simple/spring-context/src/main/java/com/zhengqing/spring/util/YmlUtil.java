package com.zhengqing.spring.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * <p> yml配置读取工具类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/10/10 16:36
 */
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
        if (CollUtil.isEmpty(source)) {
            return configMap;
        }
        source.forEach((key, val) -> {
            String keyNew = StrUtil.isBlank(prefix) ? key : prefix + key;
            if (val instanceof Map) {
                loadAsPlainMap(configMap, (Map) val, keyNew + ".");
            } else {
                configMap.put(keyNew, val);
            }
        });
        return configMap;
    }

}
