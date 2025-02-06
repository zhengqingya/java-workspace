package com.zhengqing.mybatis.executor.parameter;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * <p> 参数处理器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/26 21:26
 */
public interface ParameterHandler {

    void setParam(PreparedStatement ps, Object parameter, List<String> parameterMappings);

}
