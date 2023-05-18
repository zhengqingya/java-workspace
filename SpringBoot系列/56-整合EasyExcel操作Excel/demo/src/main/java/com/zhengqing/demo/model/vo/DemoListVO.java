package com.zhengqing.demo.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 测试demo展示视图
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("测试demo展示视图")
public class DemoListVO {

    @ExcelProperty(value = "主键ID", index = 0)
    @ApiModelProperty("主键ID")
    private Integer id;

    @ColumnWidth(50)
    @ExcelProperty(value = "用户名", index = 1)
    @ApiModelProperty("用户名")
    private String username;

    @ColumnWidth(50)
    @ExcelProperty(value = "密码", index = 2)
    @ApiModelProperty("密码")
    private String password;
    
}
