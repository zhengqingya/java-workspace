package com.zhengqing.spring.io;

import com.zhengqing.spring.util.YmlUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.time.*;
import java.util.*;
import java.util.function.Function;


@Slf4j
public class PropertyResolver {

    // 存储所有配置项
    Map<String, Object> properties = new HashMap<>();
    Map<Class<?>, Function<String, Object>> converters = new HashMap<>();

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

        // register converters
        converters.put(String.class, s -> s);
        converters.put(boolean.class, s -> Boolean.parseBoolean(s));
        converters.put(Boolean.class, s -> Boolean.valueOf(s));

        converters.put(byte.class, s -> Byte.parseByte(s));
        converters.put(Byte.class, s -> Byte.valueOf(s));

        converters.put(short.class, s -> Short.parseShort(s));
        converters.put(Short.class, s -> Short.valueOf(s));

        converters.put(int.class, s -> Integer.parseInt(s));
        converters.put(Integer.class, s -> Integer.valueOf(s));

        converters.put(long.class, s -> Long.parseLong(s));
        converters.put(Long.class, s -> Long.valueOf(s));

        converters.put(float.class, s -> Float.parseFloat(s));
        converters.put(Float.class, s -> Float.valueOf(s));

        converters.put(double.class, s -> Double.parseDouble(s));
        converters.put(Double.class, s -> Double.valueOf(s));

        converters.put(LocalDate.class, s -> LocalDate.parse(s));
        converters.put(LocalTime.class, s -> LocalTime.parse(s));
        converters.put(LocalDateTime.class, s -> LocalDateTime.parse(s));
        converters.put(ZonedDateTime.class, s -> ZonedDateTime.parse(s));
        converters.put(Duration.class, s -> Duration.parse(s));
        converters.put(ZoneId.class, s -> ZoneId.of(s));
    }

    /**
     * 获取配置项
     */
    public String getProperty(String key) {
        PropertyExpr keyExpr = parsePropertyExpr(key);
        if (keyExpr != null) {
            if (keyExpr.getDefaultValue() != null) {
                return getProperty(keyExpr.getKey(), keyExpr.getDefaultValue());
            } else {
                return getRequiredProperty(keyExpr.getKey());
            }
        }
        Object value = this.properties.get(key);
        if (value != null) {
            return parseValue(String.valueOf(value));
        }
        return (String) value;
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value == null ? parseValue(defaultValue) : value;
    }

    @Nullable
    public <T> T getProperty(String key, Class<T> targetType) {
        String value = getProperty(key);
        if (value == null) {
            return null;
        }
        return convert(targetType, value);
    }

    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return convert(targetType, value);
    }

    public String getRequiredProperty(String key) {
        String value = getProperty(key);
        return Objects.requireNonNull(value, "Property '" + key + "' not found.");
    }

    public <T> T getRequiredProperty(String key, Class<T> targetType) {
        T value = getProperty(key, targetType);
        return Objects.requireNonNull(value, "Property '" + key + "' not found.");
    }

    @SuppressWarnings("unchecked")
    <T> T convert(Class<?> clazz, String value) {
        Function<String, Object> fn = this.converters.get(clazz);
        if (fn == null) {
            throw new IllegalArgumentException("Unsupported value type: " + clazz.getName());
        }
        return (T) fn.apply(value);
    }

    String parseValue(String value) {
        PropertyExpr expr = parsePropertyExpr(value);
        if (expr == null) {
            return value;
        }
        if (expr.getDefaultValue() != null) {
            return getProperty(expr.getKey(), expr.getDefaultValue());
        } else {
            return getRequiredProperty(expr.getKey());
        }
    }

    PropertyExpr parsePropertyExpr(String key) {
        if (key.startsWith("${") && key.endsWith("}")) {
            int n = key.indexOf(':');
            if (n == (-1)) {
                // no default value: ${key}
                String k = notEmpty(key.substring(2, key.length() - 1));
                return new PropertyExpr(k, null);
            } else {
                // has default value: ${key:default}
                String k = notEmpty(key.substring(2, n));
                return new PropertyExpr(k, key.substring(n + 1, key.length() - 1));
            }
        }
        return null;
    }

    String notEmpty(String key) {
        if (key.isEmpty()) {
            throw new IllegalArgumentException("Invalid key: " + key);
        }
        return key;
    }

}
