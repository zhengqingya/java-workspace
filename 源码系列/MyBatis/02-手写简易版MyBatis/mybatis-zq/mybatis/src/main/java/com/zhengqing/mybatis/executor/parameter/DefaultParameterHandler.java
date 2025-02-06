package com.zhengqing.mybatis.executor.parameter;

import cn.hutool.core.util.ReflectUtil;
import com.zhengqing.mybatis.session.Configuration;
import com.zhengqing.mybatis.type.TypeHandler;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

/**
 * <p> 默认参数处理器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/26 21:26
 */
public class DefaultParameterHandler implements ParameterHandler {

    private Configuration configuration;

    public DefaultParameterHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @SneakyThrows
    @Override
    public void setParam(PreparedStatement ps, Object parameter, List<String> parameterMappings) {
        // 设置值
        Map<Class, TypeHandler> typeHandlerMap = this.configuration.getTypeHandlerMap();
        Map<String, Object> paramValueMap = (Map<String, Object>) parameter;
        for (int i = 0; i < parameterMappings.size(); i++) {
            String jdbcColumnName = parameterMappings.get(i);
            if (jdbcColumnName.contains(".")) {
                String[] split = jdbcColumnName.split("\\.");
                String key = split[0];
                Object instanceValue = paramValueMap.get(key);
                Object fieldValue = ReflectUtil.getFieldValue(instanceValue, split[1]);
                typeHandlerMap.get(fieldValue.getClass()).setParameter(ps, i + 1, fieldValue);
            } else {
                Object val = paramValueMap.get(jdbcColumnName);
                typeHandlerMap.get(val.getClass()).setParameter(ps, i + 1, val);
            }
        }
    }
}
