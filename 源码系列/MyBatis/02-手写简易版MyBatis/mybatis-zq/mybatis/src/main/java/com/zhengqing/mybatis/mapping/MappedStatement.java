package com.zhengqing.mybatis.mapping;

import com.zhengqing.mybatis.cache.Cache;
import com.zhengqing.mybatis.parsing.GenericTokenParser;
import com.zhengqing.mybatis.parsing.ParameterMappingTokenHandler;
import com.zhengqing.mybatis.scripting.DynamicContext;
import com.zhengqing.mybatis.scripting.SqlNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

/**
 * <p> mapper配置信息 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/22 18:16
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MappedStatement {

    private String id; // 唯一标识 eg: com.zhengqing.demo.mapper.UserMapper.selectList
    private String sql; // SQL eg: select * from t_user where id = #{id}
    private Class returnType;// 返回类型
    private SqlCommandType sqlCommandType; // SQL命令类型
    private Boolean isSelectMany; // 是否查询多条数据
    private Cache cache; // 缓存
    private SqlNode sqlSource; // 动态SQL

    public BoundSql getBoundSql(Object parameter) {
        if (this.sqlSource != null) {
            DynamicContext dynamicContext = new DynamicContext((Map<String, Object>) parameter);
            this.sqlSource.apply(dynamicContext);
            this.sql = dynamicContext.getSql();
        }

        // sql解析  #{}  --- ?
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String sql = genericTokenParser.parse(this.sql);
        List<String> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        return BoundSql.builder().sql(sql).parameterMappings(parameterMappings).build();
    }


    public String getCacheKey(Object parameter) {
        return this.id + ":" + this.sql + ":" + parameter;
    }

}
