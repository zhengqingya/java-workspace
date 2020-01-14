package com.zhengqing.demo.modules.system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * <p> 测试 </p>
 *
 * @author : zhengqing
 * @description : indexName:索引库     type:类型（可理解为mysql数据库中的表名）
 * @date : 2019/12/27 14:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "zq_test", type = "user")
public class User {
    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private String name;

    //    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String intro;

    @Field(type = FieldType.Integer)
    private Integer age;
}
