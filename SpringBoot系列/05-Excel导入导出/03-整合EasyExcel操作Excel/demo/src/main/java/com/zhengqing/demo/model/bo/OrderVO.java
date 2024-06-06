package com.zhengqing.demo.model.bo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.zhengqing.demo.easyexcel.converter.FenToYuanConverter;
import com.zhengqing.demo.easyexcel.converter.YesNoConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>  </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/1/25 14:13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//内容行高
@ContentRowHeight(15)
//表头行高
@HeadRowHeight(20)
public class OrderVO {

    @ColumnWidth(12)
    @ExcelProperty(value = {"动态合并单元格", "序号"}, index = 0)
    private Integer id;


    @ColumnWidth(25)
    @ExcelProperty(value = {"动态合并单元格", "名称"}, index = 1)
    private String name;

    @ColumnWidth(12)
    @ExcelProperty(value = {"动态合并单元格", "类型"}, index = 2, converter = YesNoConverter.class)
    private Integer type;


    @ColumnWidth(25)
    @ExcelProperty(value = {"动态合并单元格", "创建时间"}, index = 3)
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    private String createTime;


    @ColumnWidth(20)
    @ExcelProperty(value = {"动态合并单元格", "商品"}, index = 4)
    private String productName;


    @ColumnWidth(15)
    @ExcelProperty(value = {"动态合并单元格", "价格"}, index = 5, converter = FenToYuanConverter.class)
    @NumberFormat("#.##元")
    private Integer productPrice;

    @ExcelIgnore
    private String updateTime;


}
