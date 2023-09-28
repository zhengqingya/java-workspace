package com.zhengqing.demo.api;

import cn.hutool.core.date.DateUtil;
import com.zhengqing.demo.model.dto.FileUploadDTO;
import com.zhengqing.demo.model.vo.ApiResult;
import com.zhengqing.demo.model.vo.FileUploadVO;
import com.zhengqing.demo.service.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * <p> 文件上传 接口 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/01/05 16:40
 */
@RestController
@RequestMapping("/api/file")
@Api(tags = {"文件上传"})
public class FileController {

    @Resource
    private IFileService fileService;

    @GetMapping("time")
    public ApiResult<String> time() {
        return ApiResult.ok(DateUtil.today());
    }

    @ApiOperation("分片上传")
    @PostMapping("upload")
    public ApiResult<FileUploadVO> upload(@Validated @RequestBody FileUploadDTO params) {
        return ApiResult.ok(this.fileService.upload(params));
    }

    @ApiOperation("获取每个分片的预签名上传地址")
    @GetMapping("/{fileMd5}/{partNumber}")
    public ApiResult<String> getPreSignUploadUrl(@PathVariable String fileMd5, @PathVariable Integer partNumber) {
        return ApiResult.ok(this.fileService.getPreSignUploadUrl(fileMd5, partNumber));
    }

    @ApiOperation("合并分片")
    @PostMapping("/merge/{fileMd5}")
    public ApiResult<Boolean> merge(@PathVariable String fileMd5) {
        this.fileService.merge(fileMd5);
        return ApiResult.ok(true);
    }

}
