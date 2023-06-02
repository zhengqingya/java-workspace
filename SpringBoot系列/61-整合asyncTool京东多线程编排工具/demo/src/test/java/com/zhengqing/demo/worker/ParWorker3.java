package com.zhengqing.demo.worker;

import com.jd.platform.async.callback.ICallback;
import com.jd.platform.async.callback.IWorker;
import com.jd.platform.async.worker.WorkResult;
import com.jd.platform.async.wrapper.WorkerWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class ParWorker3 implements IWorker<String, String>, ICallback<String, String> {


    @Override
    public String action(String s, Map<String, WorkerWrapper> map) {
        System.out.println("[worker3] do...");
        return "worker3";
    }

    @Override
    public String defaultValue() {
        return "worker3--default";
    }

    @Override
    public void begin() {
        System.out.println("[worker3] " + Thread.currentThread().getName() + "- start --");
    }

    @Override
    public void result(boolean success, String param, WorkResult<String> workResult) {
        log.debug("[{}] {}", success, workResult);
    }

}