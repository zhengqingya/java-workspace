package com.zhengqing.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * excel导出模板枚举类
 * </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/9/7 13:56
 */
@Getter
@AllArgsConstructor
public enum ExcelExportFileTypeEnum {

    测试("/测试导出模板.xls", "测试");

    /**
     * 导出模板文件
     */
    private String templateFile;
    /**
     * 导出表格名
     */
    private String sheetName;

}
