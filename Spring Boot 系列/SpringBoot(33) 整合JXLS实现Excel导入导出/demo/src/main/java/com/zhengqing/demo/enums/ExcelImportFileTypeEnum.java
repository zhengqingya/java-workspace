package com.zhengqing.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * excel导入解析枚举类
 * </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/9/7 14:23
 */
@Getter
@AllArgsConstructor
public enum ExcelImportFileTypeEnum {

    测试("/测试.xml");

    /**
     * 导入映射文件XML
     */
    private String mappingXml;

}
