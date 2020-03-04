package com.zhengqing.demo;

import com.qiniu.common.QiniuException;
import com.zhengqing.demo.modules.qiniu.service.IQiniuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class QiniuTest {

    @Autowired
    private IQiniuService qiniuService;

    @Test
    public void testUpload() throws QiniuException {
        String result = qiniuService.uploadFile(new File("D:\\IT_zhengqing\\code\\document\\image\\头像系列\\头像系列3.jpg"), "helloworld");
        System.out.println("访问地址： " + result);
    }

    @Test
    public void testDelete() throws QiniuException {
        String result = qiniuService.delete("helloworld");
        System.out.println(result);
    }

}
