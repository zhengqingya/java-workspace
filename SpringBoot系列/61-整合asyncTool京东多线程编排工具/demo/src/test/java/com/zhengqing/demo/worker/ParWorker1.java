package com.zhengqing.demo.worker;

import com.jd.platform.async.callback.ICallback;
import com.jd.platform.async.callback.IWorker;
import com.jd.platform.async.worker.WorkResult;
import com.jd.platform.async.wrapper.WorkerWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class ParWorker1 implements IWorker<String, String>, ICallback<String, String> {


    @Override
    public String action(String s, Map<String, WorkerWrapper> map) {
        System.out.println("[worker1] do...");
        return "worker1";
    }

    @Override
    public String defaultValue() {
        return "worker1--default";
    }

    @Override
    public void begin() {
        System.out.println("[worker1] " + Thread.currentThread().getName() + "- start --");
    }

    @Override
    public void result(boolean success, String param, WorkResult<String> workResult) {
        log.debug("[{}] {}", success, workResult);
    }

}