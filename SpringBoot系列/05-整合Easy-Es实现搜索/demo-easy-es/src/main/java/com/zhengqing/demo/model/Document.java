package com.zhengqing.demo.model;

import cn.easyes.annotation.HighLight;
import cn.easyes.annotation.IndexField;
import cn.easyes.annotation.IndexId;
import cn.easyes.annotation.IndexName;
import cn.easyes.annotation.rely.Analyzer;
import cn.easyes.annotation.rely.FieldType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> ES 数据模型 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/11/21 17:35
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IndexName("document")
public class Document {
    /**
     * es中的唯一id
     */
    @IndexId
    @ApiModelProperty("es中的唯一id")
    private String id;

    /**
     * 文档标题
     */
    @ApiModelProperty(value = "文档标题", example = "zhengqingya")
    private String title;
    /**
     * 文档内容
     */
    @ApiModelProperty(value = "文档内容", example = "this is my test data...")
    // 高亮查询
    @HighLight(preTag = "<em>", postTag = "</em>")
    // 分词查询
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_MAX_WORD, searchAnalyzer = Analyzer.IK_MAX_WORD)
    private String content;

}
