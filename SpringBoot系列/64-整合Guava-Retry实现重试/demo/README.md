# Guava-Retry

- https://github.com/rholder/guava-retrying

与`spring-retry`类似，可实现重试

`guava-retry`在使用上更便捷，更灵活，能根据方法返回值来判断是否重试，而`spring-retry`只能根据抛出的异常来进行重试。

代码见 [RetryTest.java](src/test/java/com/zhengqing/demo/RetryTest.java)

---

参考笔记

- https://mp.weixin.qq.com/s/JaJ5ab8sAseO-CvQnrkToA
- https://cloud.tencent.com/developer/article/1752086
