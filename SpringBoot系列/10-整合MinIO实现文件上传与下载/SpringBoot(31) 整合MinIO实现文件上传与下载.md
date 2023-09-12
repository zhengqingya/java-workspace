### 一、前言

`MinIO` 是一个基于`Apache License v2.0`开源协议的`对象存储`服务。它兼容亚马逊S3云存储服务接口，非常适合于存储`大容量非结构化`的数据，例如`图片`、`视频`、`日志文件`、`备份数据`和`容器/虚拟机镜像`等，而一个对象文件可以是任意大小，从几kb到最大5T不等。

MinIO官方文档：[https://docs.min.io/cn/](https://docs.min.io/cn/)

### 二、Docker安装MinIO

```shell
# 环境准备
git clone https://gitee.com/zhengqingya/docker-compose.git

# Liunx系统走此路径
cd docker-compose/Liunx
# Windows系统走此路径
cd docker-compose/Windows

# 运行服务
docker-compose -f docker-compose-minio.yml -p minio up -d
```

访问地址：[`http://127.0.0.1:9001/minio`](http://127.0.0.1:9001/minio)
登录账号密码：`root/password`

![](./images/20230912141304646.png)


### 三、SpringBoot整合MinIO实现文件上传与下载

#### 1、`pom.xml`中引入`MinIO`依赖

```xml
<dependency>
  <groupId>io.minio</groupId>
  <artifactId>minio</artifactId>
  <version>7.1.0</version>
</dependency>
```

#### 2、`application.yml`中配置`MinIO`

```yml
# ====================== ↓↓↓↓↓↓ MinIO文件服务器 ↓↓↓↓↓↓ ======================
minio:
  url: http://127.0.0.1:9001
  accessKey: root
  secretKey: password
  bucketName: test
```

#### 3、MinIO属性类

```java
@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinIoProperties {

    /**
     * minio地址+端口号
     */
    private String url;

    /**
     * minio用户名
     */
    private String accessKey;

    /**
     * minio密码
     */
    private String secretKey;

    /**
     * 文件桶的名称
     */
    private String bucketName;

}
```

#### 4、MinIO工具类

> Java Client API参考文档：https://docs.min.io/cn/java-client-api-reference.html

```java
@Slf4j
@Component
public class MinIoUtil {

    @Autowired
    MinIoProperties minIoProperties;

    private static MinioClient minioClient;

    /**
     * 初始化minio配置
     *
     * @param :
     * @return: void
     * @date : 2020/8/16 20:56
     */
    @PostConstruct
    public void init() {
        try {
            minioClient = new MinioClient(minIoProperties.getUrl(), minIoProperties.getAccessKey(),
                minIoProperties.getSecretKey());
            createBucket(minIoProperties.getBucketName());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("初始化minio配置异常: 【{}】", e.fillInStackTrace());
        }
    }

    /**
     * 判断 bucket是否存在
     *
     * @param bucketName:
     *            桶名
     * @return: boolean
     * @date : 2020/8/16 20:53
     */
    @SneakyThrows(Exception.class)
    public static boolean bucketExists(String bucketName) {
        return minioClient.bucketExists(bucketName);
    }

    /**
     * 创建 bucket
     *
     * @param bucketName:
     *            桶名
     * @return: void
     * @date : 2020/8/16 20:53
     */
    @SneakyThrows(Exception.class)
    public static void createBucket(String bucketName) {
        boolean isExist = minioClient.bucketExists(bucketName);
        if (!isExist) {
            minioClient.makeBucket(bucketName);
        }
    }

    /**
     * 获取全部bucket
     *
     * @param :
     * @return: java.util.List<io.minio.messages.Bucket>
     * @date : 2020/8/16 23:28
     */
    @SneakyThrows(Exception.class)
    public static List<Bucket> getAllBuckets() {
        return minioClient.listBuckets();
    }

    /**
     * 文件上传
     *
     * @param bucketName:
     *            桶名
     * @param fileName:
     *            文件名
     * @param filePath:
     *            文件路径
     * @return: void
     * @date : 2020/8/16 20:53
     */
    @SneakyThrows(Exception.class)
    public static void upload(String bucketName, String fileName, String filePath) {
        minioClient.putObject(bucketName, fileName, filePath, null);
    }

    /**
     * 文件上传
     *
     * @param bucketName:
     *            桶名
     * @param fileName:
     *            文件名
     * @param stream:
     *            文件流
     * @return: java.lang.String : 文件url地址
     * @date : 2020/8/16 23:40
     */
    @SneakyThrows(Exception.class)
    public static String upload(String bucketName, String fileName, InputStream stream) {
        minioClient.putObject(bucketName, fileName, stream, new PutObjectOptions(stream.available(), -1));
        return getFileUrl(bucketName, fileName);
    }

    /**
     * 文件上传
     *
     * @param bucketName:
     *            桶名
     * @param file:
     *            文件
     * @return: java.lang.String : 文件url地址
     * @date : 2020/8/16 23:40
     */
    @SneakyThrows(Exception.class)
    public static String upload(String bucketName, MultipartFile file) {
        final InputStream is = file.getInputStream();
        final String fileName = file.getOriginalFilename();
        minioClient.putObject(bucketName, fileName, is, new PutObjectOptions(is.available(), -1));
        is.close();
        return getFileUrl(bucketName, fileName);
    }

    /**
     * 删除文件
     *
     * @param bucketName:
     *            桶名
     * @param fileName:
     *            文件名
     * @return: void
     * @date : 2020/8/16 20:53
     */
    @SneakyThrows(Exception.class)
    public static void deleteFile(String bucketName, String fileName) {
        minioClient.removeObject(bucketName, fileName);
    }

    /**
     * 下载文件
     *
     * @param bucketName:
     *            桶名
     * @param fileName:
     *            文件名
     * @param response:
     * @return: void
     * @date : 2020/8/17 0:34
     */
    @SneakyThrows(Exception.class)
    public static void download(String bucketName, String fileName, HttpServletResponse response) {
        // 获取对象的元数据
        final ObjectStat stat = minioClient.statObject(bucketName, fileName);
        response.setContentType(stat.contentType());
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        InputStream is = minioClient.getObject(bucketName, fileName);
        IOUtils.copy(is, response.getOutputStream());
        is.close();
    }

    /**
     * 获取minio文件的下载地址
     *
     * @param bucketName:
     *            桶名
     * @param fileName:
     *            文件名
     * @return: java.lang.String
     * @date : 2020/8/16 22:07
     */
    @SneakyThrows(Exception.class)
    public static String getFileUrl(String bucketName, String fileName) {
        return minioClient.presignedGetObject(bucketName, fileName);
    }

}
```

#### 5、MinIO测试接口

```java
@RestController
@RequestMapping("/api/minio")
@Api(tags = {"MinIO测试接口"})
public class MinIoController {

    @Autowired
    MinIoProperties minIoProperties;

    @ApiOperation(value = "上传文件")
    @PostMapping(value = "/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {
        String fileUrl = MinIoUtil.upload(minIoProperties.getBucketName(), file);
        return "文件下载地址：" + fileUrl;
    }

    @ApiOperation(value = "下载文件")
    @GetMapping(value = "/download")
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        MinIoUtil.download(minIoProperties.getBucketName(), fileName, response);
    }

    @ApiOperation(value = "删除文件")
    @GetMapping(value = "/delete")
    public String delete(@RequestParam("fileName") String fileName) {
        MinIoUtil.deleteFile(minIoProperties.getBucketName(), fileName);
        return "删除成功";
    }

}
```

---

### 本文案例demo源码

[https://gitee.com/zhengqingya/java-workspace](https://gitee.com/zhengqingya/java-workspace)

---

> 今日分享语句：
> 努力是一种生活态度，与年龄无关。所以，无论什么时候，千万不可放纵自己，给自己找懒散和拖延的借口，对自己严格一点儿，时间长了，努力便成为一种心理习惯，一种生活方式！
