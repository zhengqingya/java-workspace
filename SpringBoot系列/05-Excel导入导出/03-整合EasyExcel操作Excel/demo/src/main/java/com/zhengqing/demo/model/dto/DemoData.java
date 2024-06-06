package com.zhengqing.demo.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DemoData {
    /**
     * 强制读取第1个 这里不建议 index 和 name 同时用，要么一个对象只用index，要么一个对象只用name去匹配
     */
    @ExcelProperty(index = 0)
    private String string;

    @ExcelProperty("日期标题")
    private Date date;

    @ExcelProperty("数字标题")
    private Double doubleData;

    @ExcelProperty(index = 3)
    private String equation;

}
