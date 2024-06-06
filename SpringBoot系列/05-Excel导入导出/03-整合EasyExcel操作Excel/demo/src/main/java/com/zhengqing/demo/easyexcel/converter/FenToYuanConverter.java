package com.zhengqing.demo.easyexcel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.NumberUtils;

import java.math.BigDecimal;


/**
 * <p> EasyExcel 自定义格式转换 -- 分转元 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/1/25 16:02
 */
public class FenToYuanConverter implements Converter<Integer> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
                                     GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }

    @Override
    public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) {
        BigDecimal result = new BigDecimal(value).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        return NumberUtils.formatToCellData(result, contentProperty);
    }

}
