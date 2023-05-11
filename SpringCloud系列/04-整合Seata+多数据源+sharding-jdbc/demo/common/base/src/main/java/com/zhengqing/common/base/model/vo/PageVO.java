package com.zhengqing.common.base.model.vo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * <p> 分页响应参数 </p>
 *
 * @author zhengqingya
 * @description {@link com.baomidou.mybatisplus.core.metadata.IPage }
 * @date 2021/8/18 16:14
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("基类响应参数")
public class PageVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页
     */
    @JsonSerialize(using = ToLongSerializer.class)
    Long current;
    /**
     * 每页显示条数
     */
    @JsonSerialize(using = ToLongSerializer.class)
    Long size;
    /**
     * 当前分页总页数
     */
    @JsonSerialize(using = ToLongSerializer.class)
    Long pages;
    /**
     * 当前满足条件总行数
     */
    @JsonSerialize(using = ToLongSerializer.class)
    Long total;
    /**
     * 分页记录列表
     */
    List<T> records;

}

/**
 * <p> jackson 转Long类型 </p>
 *
 * @author zhengqingya
 * @description 解决全局将Long类型转换为String类型后，部分类又需要Long还是原本Long类型问题
 * 使用方式：添加注解`@JsonSerialize(using = ToLongSerializer.class)`
 * {@link com.zhengqing.common.web.config.jackson.ToLongSerializer }
 * 由于此为base包，不额外引用其它包中的东西，这里单独处理一下
 * @date 2022/8/5 17:22
 */
class ToLongSerializer extends JsonSerializer<Long> {

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value);
    }

}