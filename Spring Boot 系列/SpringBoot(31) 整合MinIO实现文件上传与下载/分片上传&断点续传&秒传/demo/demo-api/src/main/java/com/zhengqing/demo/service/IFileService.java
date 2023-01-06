package com.zhengqing.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhengqing.demo.entity.File;
import com.zhengqing.demo.model.dto.FileUploadDTO;
import com.zhengqing.demo.model.vo.FileUploadVO;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p>  文件上传记录 服务类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/01/05 16:40
 */
public interface IFileService extends IService<File> {


    /**
     * 上传
     *
     * @param params 提交参数
     * @return 结果
     * @author zhengqingya
     * @date 2023/1/5 16:53
     */
    FileUploadVO upload(FileUploadDTO params);


    /**
     * 获取每个分片的预签名上传地址
     *
     * @param fileMd5    文件唯一标识
     * @param partNumber 分片
     * @return 文件预签名上传地址
     * @author zhengqingya
     * @date 2023/1/5 16:53
     */
    String getPreSignUploadUrl(@PathVariable String fileMd5, @PathVariable Integer partNumber);

    /**
     * 合并分片
     *
     * @param fileMd5 文件唯一标识
     * @return void
     * @author zhengqingya
     * @date 2023/1/5 17:44
     */
    void merge(String fileMd5);
}
