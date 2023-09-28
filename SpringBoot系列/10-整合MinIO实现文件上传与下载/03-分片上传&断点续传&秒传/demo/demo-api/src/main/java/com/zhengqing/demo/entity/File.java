package com.zhengqing.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>  文件上传记录 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/01/05 16:40
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_file")
@ApiModel("文件上传记录")
public class File extends Model<File> {

    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("分片上传的uploadId")
    private String uploadId;

    @ApiModelProperty("文件唯一标识（md5）")
    private String fileMd5;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("桶名")
    private String bucketName;

    @ApiModelProperty("文件存储路径")
    private String objectName;

    @ApiModelProperty("文件大小（byte）")
    private Long fileSize;

    @ApiModelProperty("每个分片大小（byte）")
    private Long chunkSize;

    @ApiModelProperty("分片数量")
    private Integer chunkNum;

    @ApiModelProperty("是否上传完成(1：是  0：否)")
    private Boolean isFinish;

}
