package com.zhengqing.demo.modules.common.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p> Excel表头信息 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2019/11/27 18:14
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class ExcelHeaderInfo {

    /**
     * 标题的首行坐标
     */
    private int firstRow;
    /**
     * 标题的末行坐标
     */
    private int lastRow;
    /**
     * 标题的首列坐标
     */
    private int firstCol;
    /**
     * 标题的末列坐标
     */
    private int lastCol;
    /**
     * 标题
     */
    private String title;

}
