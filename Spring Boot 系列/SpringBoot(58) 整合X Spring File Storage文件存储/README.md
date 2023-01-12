# X Spring File Storage

https://spring-file-storage.xuyanwu.cn

在 SpringBoot 中通过简单的方式将文件存储到 本地、FTP、SFTP、WebDAV、谷歌云存储、阿里云OSS、华为云OBS、七牛云Kodo、腾讯云COS、百度云
BOS、又拍云USS、MinIO、 AWS S3、金山云 KS3、美团云 MSS、京东云 OSS、天翼云 OOS、移动云 EOS、沃云 OSS、 网易数帆 NOS、Ucloud US3、青云
QingStor、平安云 OBS、首云 OSS、IBM COS、其它兼容 S3 协议的平台

---

### MinIO

#### 1、pom.xml

```
<!-- spring-file-storage -->
<dependency>
    <groupId>cn.xuyanwu</groupId>
    <artifactId>spring-file-storage</artifactId>
    <version>0.5.0</version>
</dependency>
<!-- minio文件服务器 -->
<!-- https://mvnrepository.com/artifact/io.minio/minio -->
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>8.4.6</version>
</dependency>
<!-- 解决minio报错问题 -->
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>4.8.1</version>
</dependency>
```

#### 2、application.yml

```yml
spring:
  file-storage: #文件存储配置
    default-platform: minio-1 #默认使用的存储平台
    thumbnail-suffix: ".min.jpg" #缩略图后缀，例如【.min.jpg】【.png】
    minio: # MinIO，由于 MinIO SDK 支持 AWS S3，其它兼容 AWS S3 协议的存储平台也都可配置在这里
      - platform: minio-1 # 存储平台标识
        enable-storage: true  # 启用存储
        access-key: admin
        secret-key: password
        end-point: http://127.0.0.1:9001
        bucket-name: test
        domain: "http://127.0.0.1:9001/" # 访问域名，注意“/”结尾，例如：http://minio.abc.com/abc/
        base-path: zhengqingya/ # 基础路径
```

#### 3、测试文件上传

```java
package com.zhengqing.demo.api;

import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileDetailController {

    @Autowired
    private FileStorageService fileStorageService;//注入实列

    /**
     * 上传文件，成功返回文件 url
     */
    @PostMapping("/upload")
    public String upload(@RequestPart MultipartFile file) {
        FileInfo fileInfo = this.fileStorageService.of(file)
                .setPath("upload/") //保存到相对路径下，为了方便管理，不需要可以不写
                .setObjectId("0")   //关联对象id，为了方便管理，不需要可以不写
                .setObjectType("0") //关联对象类型，为了方便管理，不需要可以不写
                .putAttr("role", "admin") //保存一些属性，可以在切面、保存上传记录、自定义存储平台等地方获取使用，不需要可以不写
                .upload();  //将文件上传到对应地方
        return fileInfo == null ? "上传失败！" : fileInfo.getUrl();
    }

    /**
     * 上传图片，成功返回文件信息
     * 图片处理使用的是 https://github.com/coobird/thumbnailator
     */
    @PostMapping("/upload-image")
    public FileInfo uploadImage(@RequestPart MultipartFile file) {
        return this.fileStorageService.of(file)
                .image(img -> img.size(1000, 1000))  //将图片大小调整到 1000*1000
                .thumbnail(th -> th.size(200, 200))  //再生成一张 200*200 的缩略图
                .upload();
    }

    /**
     * 上传文件到指定存储平台，成功返回文件信息
     */
    @PostMapping("/upload-platform")
    public FileInfo uploadPlatform(@RequestPart MultipartFile file) {
        return this.fileStorageService.of(file)
                .setPlatform("minio-1")    //使用指定的存储平台
                .upload();
    }
}
```

