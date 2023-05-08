package com.zhengqing.demo.modules.qiniu.service;

import com.qiniu.common.QiniuException;

import java.io.File;
import java.io.InputStream;

/**
 * <p> 七牛云-服务类$ </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/3/4$ 11:42$
 */
public interface IQiniuService {

    /**
     * 以文件的形式上传
     *
     * @param file
     * @param fileName:
     * @return: java.lang.String
     */
    String uploadFile(File file, String fileName) throws QiniuException;

    /**
     * 以流的形式上传
     *
     * @param inputStream
     * @param fileName:
     * @return: java.lang.String
     */
    String uploadFile(InputStream inputStream, String fileName) throws QiniuException;

    /**
     * 删除文件
     *
     * @param key:
     * @return: java.lang.String
     */
    String delete(String key) throws QiniuException;

}
