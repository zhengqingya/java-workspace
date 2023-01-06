package com.zhengqing.demo.api;

import com.zhengqing.demo.config.MinIoProperties;
import com.zhengqing.demo.minio.MinIoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * MinIO测试接口
 * </p>
 *
 * @author zhengqing
 * @description
 * @date 2020/8/16 20:39
 */
@RestController
@RequestMapping("/api/minio")
@Api(tags = {"MinIO测试接口"})
public class MinIoSimpleController {

    @Resource
    private MinIoProperties minIoProperties;

    @ApiOperation(value = "上传文件")
    @PostMapping(value = "/upload")
    public String upload(@RequestPart @RequestParam MultipartFile file) {
        return MinIoUtil.upload(this.minIoProperties.getBucketName(), file);
    }

    @ApiOperation(value = "下载文件")
    @GetMapping(value = "/download")
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        MinIoUtil.download(this.minIoProperties.getBucketName(), fileName, response);
    }

    @ApiOperation(value = "删除文件")
    @GetMapping(value = "/delete")
    public String delete(@RequestParam("fileName") String fileName) {
        MinIoUtil.deleteFile(this.minIoProperties.getBucketName(), fileName);
        return "删除成功";
    }

}
