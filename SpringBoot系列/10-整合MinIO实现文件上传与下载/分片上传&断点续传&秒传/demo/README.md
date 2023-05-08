# 说明

参考 https://gitee.com/Gary2016/minio-upload

将后端的s3

```
<dependency>
    <groupId>com.amazonaws</groupId>
    <artifactId>aws-java-sdk-s3</artifactId>
    <version>1.12.263</version>
</dependency>
```

修改为minio

```
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>8.4.6</version>
</dependency>
```