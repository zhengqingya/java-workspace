package com.zhengqing.demo;

import org.csource.fastdfs.*;
import org.junit.Test;

/**
 * <p>
 * 小测试$
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/7/10 11:02
 */

public class DemoTest {

    @Test
    public void testUpload() throws Exception {
        // 1.将FastDFS提供的jar包导入
        // 2.初始化全局变量。加载配置文件
        ClientGlobal.init(
                "D:\\zhengqingya\\code\\workspace-me\\java-workspace\\SpringBoot系列\\58-整合FastDFS\\demo\\src\\main\\resources\\fast_client.conf");
        // 3.创建一个TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        // 4.创建一个TrackerServer对象
        TrackerServer trackerServer = trackerClient.getConnection();
        // 5.声明一个StorageServer
        StorageServer storageServer = null;
        // 6.获得StorageClient对象
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        // 7.通过StorageClient对象方法上传
        String[] strings = storageClient.upload_file("D:\\zhengqingya\\test\\test.jpg", "jpg", null);
        // 8.获得返回信息
        for (String string : strings) {
            System.out.println(string);
        }
    }

}
