package com.zhengqing.demo;

import com.jd.platform.async.executor.Async;
import com.jd.platform.async.wrapper.WorkerWrapper;
import com.zhengqing.demo.worker.ParWorker1;
import com.zhengqing.demo.worker.ParWorker2;
import com.zhengqing.demo.worker.ParWorker3;
import org.junit.Test;

/**
 * <p> 测试 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/6/2 10:24
 */
public class AppTest {

    @Test
    public void test() throws Exception {
        ParWorker1 w1 = new ParWorker1();
        ParWorker2 w2 = new ParWorker2();
        ParWorker3 w3 = new ParWorker3();

        WorkerWrapper<String, String> workerWrapper3 = new WorkerWrapper.Builder<String, String>()
                .worker(w3)
                .callback(w3)
                .param("3")
                .build();

        WorkerWrapper<String, String> workerWrapper2 = new WorkerWrapper.Builder<String, String>()
                .worker(w2)
                .callback(w2)
                .param("2")
                .next(workerWrapper3)
                .build();

        WorkerWrapper<String, String> workerWrapper1 = new WorkerWrapper.Builder<String, String>()
                .worker(w1)
                .callback(w1)
                .param("1")
                .next(workerWrapper3)
                .build();

        Async.beginWork(1500, workerWrapper1, workerWrapper2);

        System.out.println(Async.getThreadCount());

        System.out.println(workerWrapper1.getWorkResult());

        Async.shutDown();

    }

}
