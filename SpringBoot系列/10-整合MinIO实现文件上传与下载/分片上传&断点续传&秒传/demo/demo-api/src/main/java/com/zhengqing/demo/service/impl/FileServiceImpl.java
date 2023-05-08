package com.zhengqing.demo.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhengqing.demo.config.MinIoProperties;
import com.zhengqing.demo.constant.MybatisConstant;
import com.zhengqing.demo.entity.File;
import com.zhengqing.demo.mapper.FileMapper;
import com.zhengqing.demo.minio.MyMinioClient;
import com.zhengqing.demo.model.dto.FileUploadDTO;
import com.zhengqing.demo.model.vo.FileUploadVO;
import com.zhengqing.demo.service.IFileService;
import io.minio.ListPartsResponse;
import io.minio.messages.Part;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> 文件上传记录 服务实现类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/01/05 16:40
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

    private final FileMapper fileMapper;
    private final MyMinioClient myMinioClient;
    private final MinIoProperties minIoProperties;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileUploadVO upload(FileUploadDTO params) {
        String fileMd5 = params.getFileMd5();
        String fileName = params.getFileName();
        Long fileSize = params.getFileSize();
        Long chunkSize = params.getChunkSize();
        // 分片数量
        int chunkNum = fileSize > chunkSize ? (int) Math.ceil(fileSize / chunkSize) : 1;
        String bucketName = this.getBucket();
        // 拿到存储的文件对象路径
        String objectName = DateUtil.today() + "/" + IdUtil.randomUUID() + "." + fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

        List<Part> exitPartList = Lists.newArrayList();
        boolean isFinish = false;

        // 根据文件唯一标识md5判断之前是否上传过此文件
        File file = this.getFileByMd5(fileMd5);
        if (file == null) {
            // 情景1、创建任务进行上传...
            String uploadId = this.myMinioClient.getUploadId(bucketName, objectName);

            // 分片上传：获取每个分片的预签名上传地址
            // tips: MinIO 定义分片索引是从1开始的...
//            List<String> presignedObjectUrlList = Lists.newArrayList();
//            for (int partNumber = 1; partNumber <= chunkNum; partNumber++) {
//                Map<String, String> reqParams = new HashMap<>();
//                reqParams.put("partNumber", String.valueOf(partNumber));
//                reqParams.put("uploadId", uploadId);
//                presignedObjectUrlList.add(this.myMinioClient.getPresignedObjectUrl(bucketName, objectName, reqParams));
//            }

            // DB记录数据
            File.builder()
                    .uploadId(uploadId)
                    .fileMd5(fileMd5)
                    .fileName(fileName)
                    .bucketName(bucketName)
                    .objectName(objectName)
                    .fileSize(fileSize)
                    .chunkSize(chunkSize)
                    .chunkNum(chunkNum)
                    .isFinish(false)
                    .build()
                    .insert();
        } else {
            // 之前上传过
            // 情景2、 已上传完成
            if (file.getIsFinish()) {
                isFinish = true;
            } else {
                // 情景3、未上传完...  ==》 断点上传
                boolean isExist = this.myMinioClient.doesObjectExist(bucketName, objectName);
                if (!isExist) {
                    // 未上传完则计算还需上传的分片
                    // 先查询已上传的分片数据
                    ListPartsResponse listPartsResponse = this.myMinioClient.listMultipart(bucketName, objectName, file.getUploadId());
                    exitPartList = listPartsResponse.result().partList();
                }
            }
        }
        return FileUploadVO.builder()
                .isFinish(isFinish)
                .path(objectName)
                .taskRecord(
                        FileUploadVO.TaskRecord.builder()
                                .fileMd5(fileMd5)
                                .chunkSize(chunkSize)
                                .chunkNum(chunkNum)
                                .exitPartList(exitPartList)
                                .build()
                ).build();
    }

    @Override
    public String getPreSignUploadUrl(String fileMd5, Integer partNumber) {
        File file = this.getFileByMd5(fileMd5);
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("partNumber", String.valueOf(partNumber));
        reqParams.put("uploadId", file.getUploadId());
        return this.myMinioClient.getPresignedObjectUrl(file.getBucketName(), file.getObjectName(), reqParams);
    }

    @Override
    public void merge(String fileMd5) {
        File file = this.getFileByMd5(fileMd5);
        String bucketName = file.getBucketName();
        String uploadId = file.getUploadId();
        String objectName = file.getObjectName();
        Part[] parts = new Part[10000];
        ListPartsResponse partResult = this.myMinioClient.listMultipart(bucketName, objectName, uploadId);
        List<Part> partList = partResult.result().partList();
        int partNumber = 1;
        for (Part part : partList) {
            parts[partNumber - 1] = new Part(partNumber, part.etag());
            partNumber++;
        }
        this.myMinioClient.completeMultipartUpload(bucketName, objectName, uploadId, parts);

        // 更新记录
        file.setIsFinish(true);
        file.updateById();
    }

    private File getFileByMd5(String fileMd5) {
        return this.fileMapper.selectOne(new LambdaQueryWrapper<File>().eq(File::getFileMd5, fileMd5).last(MybatisConstant.LIMIT_ONE));
    }

    private String getBucket() {
        return this.minIoProperties.getBucketName();
    }

}
