package com.zhengqing.demo.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * EasyExcel 业务服务类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
public interface IEasyExcelReadService {


    /**
     * 最简单的读
     *
     * @param file 提交文件
     * @return void
     * @author zhengqingya
     * @date 2021/01/13 10:11
     */
    void simpleRead(MultipartFile file);

}
