package com.zhengqing.mybatis.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * <p> sql & 参数映射 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/23 17:40
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BoundSql {

    private String sql;
    private List<String> parameterMappings;

}
