package com.zhengqing.demo.service;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * EasyExcel 业务服务类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
public interface IEasyExcelWriteService {


    /**
     * 最简单的写
     *
     * @return void
     * @author zhengqingya
     * @date 2022/1/25 9:27
     */
    void simpleWrite();

    /**
     * 合并单元格
     *
     * @return void
     * @author zhengqingya
     * @date 2022/1/25 9:57
     */
    void mergeWrite();

    /**
     * 文件下载
     *
     * @param response 响应
     * @return void
     * @author zhengqingya
     * @date 2022/1/25 9:27
     */
    void download(HttpServletResponse response);

    /**
     * 文件下载并且失败的时候返回json
     *
     * @param response 响应
     * @return void
     * @author zhengqingya
     * @date 2022/1/25 9:27
     */
    void downloadFailedUsingJson(HttpServletResponse response);

    /**
     * 动态合并单元格
     *
     * @param response 响应
     * @return void
     * @author zhengqingya
     * @date 2022/1/25 11:55
     */
    void dynamicMergeData(HttpServletResponse response);


}
