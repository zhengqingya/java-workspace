# TTL

- https://github.com/alibaba/transmittable-thread-local

在使用线程池等会池化复用线程的执行组件情况下，提供ThreadLocal值的传递功能，解决异步执行时上下文传递的问题。

### 一、ThreadLocal 的问题

#### 1、内存泄漏：

如果一个 ThreadLocal 变量引用的对象没有被正确地清理，那么即使线程结束了，这些对象仍然会被持有，从而导致内存泄漏。
解决方法是在不再需要时调用 ThreadLocal 的 remove() 方法来显式释放资源。

#### 2、线程池中的问题：

当使用线程池时，由于线程会被复用，因此如果不适当地管理 ThreadLocal，可能会导致数据污染或者内存泄漏。
解决方法是在线程执行完任务之后清理 ThreadLocal。


### 二、TransmittableThreadLocal (TTL)

为了解决线程池中 ThreadLocal 的问题，阿里巴巴开源了一个扩展 ThreadLocal 的库叫做 TransmittableThreadLocal (TTL)，它可以在使用线程池的情况下正确地处理 ThreadLocal 的传递和清理。

TTL 的特点

- 自动传递：在创建新线程时，自动将当前线程的 ThreadLocal 变量传递给新线程。
- 自动清理：在任务执行完毕后，自动清理 ThreadLocal 变量，防止内存泄漏。

### 三、TTL 使用示例

```
<!-- ttl -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>transmittable-thread-local</artifactId>
    <version>2.14.4</version>
</dependency>
```

ThreadLocal 问题：线程池上下文丢失

```
ThreadLocal<Integer> TL = new ThreadLocal<>();
TL.set(1);
ExecutorService executorService = ThreadUtil.newExecutor(3);
executorService.execute(() -> {
    log.info("TL:{}", TL.get()); // null
});
ThreadUtil.sleep(1, TimeUnit.SECONDS);
```

TTL 解决：

```java
@Slf4j
public class TtlTest {
    public static void main(String[] args) {
        ThreadLocal<String> TTL = new TransmittableThreadLocal<>();
        TTL.set("hello");
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> {
            TTL.set("hello-111");
            log.info("executor value: {}", TTL.get()); // "executor" 线程复用时，如果ThreadLocal中的值未remove，之后的相同线程会拿到这个线程设置的值
        });

        log.info("value: {}", TTL.get());

        TtlExecutors.getTtlExecutorService(executor).submit(() -> {
            TTL.set("hello-222");
            log.info("ttl value: {}", TTL.get());  // "ttl" 线程复用时，如果ThreadLocal中的值未remove，ttl方式会自动remove，不会保留
        });

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            executor.submit(() -> {
                log.info("executor index:{} value: {}", finalI, TTL.get()); // executor
            });
            TtlExecutors.getTtlExecutorService(executor).submit(() -> {
                log.info("ttl index:{} value: {}", finalI, TTL.get()); // ttl
            });
        }

        log.info("value: {}", TTL.get());
        executor.shutdown();
    }
}
```
