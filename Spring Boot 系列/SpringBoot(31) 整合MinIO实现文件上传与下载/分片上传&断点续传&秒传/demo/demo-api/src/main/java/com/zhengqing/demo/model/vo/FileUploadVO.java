package com.zhengqing.demo.model.vo;

import io.minio.messages.Part;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>文件上传-响应参数</p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/01/05 16:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class FileUploadVO {

    @ApiModelProperty("是否上传完成(1：是  0：否)")
    private Boolean isFinish;

    @ApiModelProperty("文件地址")
    private String path;

    @ApiModelProperty("上传记录")
    private TaskRecord taskRecord;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskRecord {

        @ApiModelProperty("文件唯一标识（md5）")
        private String fileMd5;

        @ApiModelProperty("每个分片大小（byte）")
        private Long chunkSize;

        @ApiModelProperty("分片数量")
        private Integer chunkNum;

        @ApiModelProperty("已上传完的分片")
        private List<Part> exitPartList;

    }


}
