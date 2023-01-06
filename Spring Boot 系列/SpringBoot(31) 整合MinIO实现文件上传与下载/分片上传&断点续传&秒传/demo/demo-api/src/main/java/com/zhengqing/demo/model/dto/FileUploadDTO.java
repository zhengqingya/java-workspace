package com.zhengqing.demo.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p> 文件上传记录-保存-提交参数 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/01/05 16:40
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("文件上传记录-保存-提交参数")
public class FileUploadDTO {

    @NotBlank
    @ApiModelProperty("文件唯一标识（md5）")
    private String fileMd5;

    @NotBlank
    @ApiModelProperty("文件名")
    private String fileName;

    @NotNull
    @ApiModelProperty("文件大小（byte）")
    private Long fileSize;

    @NotNull
    @ApiModelProperty("每个分片大小（byte）")
    private Long chunkSize;

}
