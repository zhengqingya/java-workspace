package com.zhengqing.demo.minio;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Part;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p> minio断点续传，分片上传，秒传扩展工具类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/1/5 17:20
 */
public class MyMinioClient extends MinioAsyncClient {
    public MyMinioClient(MinioAsyncClient client) {
        super(client);
    }

    /**
     * 判断文件是否存在
     *
     * @param bucketName 存储桶
     * @param objectName 对象
     * @return true:存在 false:失败
     * @author zhengqingya
     * @date 2023/1/5 17:20
     */
    @SneakyThrows
    public boolean doesObjectExist(String bucketName, String objectName) {
        boolean isExist = true;
        try {
            super.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            isExist = false;
        }
        return isExist;
    }

    /**
     * 上传分片上传请求，返回uploadId
     *
     * @param bucketName 存储桶
     * @param objectName 对象名
     * @return uploadId
     * @author zhengqingya
     * @date 2023/1/5 17:22
     */
    @SneakyThrows
    public String getUploadId(String bucketName, String objectName) {
        return super.createMultipartUploadAsync(bucketName, null, objectName, null, null).get().result().uploadId();
    }


    /**
     * 分片上传的预签名上传地址  过期时间为1天 put请求url
     *
     * @param bucketName  桶名
     * @param filePath    Oss文件路径
     * @param queryParams 查询参数
     * @return 分片上传的预签名上传地址
     * @author zhengqingya
     * @date 2023/1/5 17:25
     */
    @SneakyThrows
    public String getPresignedObjectUrl(String bucketName, String filePath, Map<String, String> queryParams) {
        return super.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(bucketName)
                        .object(filePath)
                        .expiry(1, TimeUnit.DAYS)
                        .extraQueryParams(queryParams)
                        .build());
    }

    /**
     * 查询分片数据
     *
     * @param bucketName 存储桶
     * @param objectName 对象名
     * @param uploadId   上传ID
     * @return io.minio.ListPartsResponse
     * @author zhengqingya
     * @date 2023/1/5 17:41
     */
    @SneakyThrows
    public ListPartsResponse listMultipart(String bucketName, String objectName, String uploadId) {
        return super.listPartsAsync(bucketName, null, objectName, 1000, 0, uploadId, null, null).get();
    }

    /**
     * 合并分片
     *
     * @param bucketName 存储桶
     * @param objectName 对象名
     * @param uploadId   上传ID
     * @param parts      分片
     * @return io.minio.ObjectWriteResponse
     * @author zhengqingya
     * @date 2023/1/5 17:49
     */
    @SneakyThrows
    public ObjectWriteResponse completeMultipartUpload(String bucketName, String objectName, String uploadId, Part[] parts) {
        return super.completeMultipartUpload(bucketName, null, objectName, uploadId, parts, null, null);
    }

}
