package com.zhengqing.demo;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.junit.Test;

import java.io.File;

/**
 * <p> 测试 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/12/30 9:27
 */
public class AppTest {

    @Test
    public void test01() throws Exception {
        File jpegFile = new File("C:\\Users\\zhengqingya\\Desktop\\头像.jpg");
        Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                //格式化输出[directory.getName()] - tag.getTagName() = tag.getDescription()
                System.out.format("[%s] - %s = %s\n",
                        directory.getName(), tag.getTagName(), tag.getDescription());
            }
            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    System.err.format("ERROR: %s", error);
                }
            }
        }

    }


}
