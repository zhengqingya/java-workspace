package com.zhengqing.demo.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class AppTask {

    private static final int HTTP_TIMEOUT = 10000;
    private static final String FINAL_URL = "http://demo-k8s-otel-java:30082/hello?name=from-php";
    private static final List<ServiceNode> SERVICE_NODE_LIST = Arrays.asList(
            new ServiceNode("java", "http://demo-k8s-otel-java:30082"),
            new ServiceNode("python", "http://demo-k8s-otel-python:30083"),
            new ServiceNode("go", "http://demo-k8s-otel-go:30084"),
            new ServiceNode("php", "http://demo-k8s-otel-php:30085")
    );

    /**
     * 定时发起 Java、Python、Go、PHP 四服务全排列链路请求。
     */
    @Scheduled(fixedRate = 1 * 60 * 1_000) // 1分钟执行1次
    public void executeFullChainRequest() {
        // 1、获取当前时间和开始时间，并生成 Java、Python、Go、PHP 的 24 条全排列链路。
        String now = DateUtil.now();
        long startTime = DateUtil.current();
        List<List<ServiceNode>> chainList = buildAllChainList();
        log.info("定时任务当前时间：{}，开始发起全链路请求，总链路数：{}", now, chainList.size());

        // 2、逐条发起链路请求，单条失败只记录日志，不影响本轮其它链路。
        int successCount = 0;
        int failCount = 0;
        for (List<ServiceNode> chain : chainList) {
            String chainName = buildChainName(chain);
            String requestUrl = buildRequestUrl(chain);
            long singleStartTime = DateUtil.current();
            try {
                log.info("发起链路请求，链路：{}，请求地址：{}", chainName, requestUrl);
                String response = HttpUtil.get(requestUrl, HTTP_TIMEOUT);
                successCount++;
                long singleCostTime = DateUtil.current() - singleStartTime;
                log.info("链路请求成功，链路：{}，执行耗时：{}ms，响应：{}", chainName, singleCostTime, response);
            } catch (Exception e) {
                failCount++;
                long singleCostTime = DateUtil.current() - singleStartTime;
                log.error("链路请求失败，链路：{}，执行耗时：{}ms，异常：{}", chainName, singleCostTime, e.getMessage(), e);
            }
        }

        // 3、输出本轮调度汇总结果，等待下一次定时调度。
        long costTime = DateUtil.current() - startTime;
        log.info("本轮全链路请求结束，总链路数：{}，成功数：{}，失败数：{}，执行耗时：{}ms", chainList.size(), successCount, failCount, costTime);
    }

    /**
     * 构建四服务全排列链路列表。
     *
     * @return 全排列链路列表
     */
    private List<List<ServiceNode>> buildAllChainList() {
        List<List<ServiceNode>> result = new ArrayList<>();
        buildChainList(new ArrayList<>(), new boolean[SERVICE_NODE_LIST.size()], result);
        return result;
    }

    /**
     * 递归生成服务链路排列。
     *
     * @param currentChain 当前链路
     * @param used         服务使用标记
     * @param result       全排列结果
     */
    private void buildChainList(List<ServiceNode> currentChain, boolean[] used, List<List<ServiceNode>> result) {
        if (currentChain.size() == SERVICE_NODE_LIST.size()) {
            result.add(new ArrayList<>(currentChain));
            return;
        }

        for (int i = 0; i < SERVICE_NODE_LIST.size(); i++) {
            if (used[i]) {
                continue;
            }
            used[i] = true;
            currentChain.add(SERVICE_NODE_LIST.get(i));
            buildChainList(currentChain, used, result);
            currentChain.remove(currentChain.size() - 1);
            used[i] = false;
        }
    }

    /**
     * 根据服务链路构建嵌套 chain 请求地址。
     *
     * @param chain 服务链路
     * @return 请求地址
     */
    private String buildRequestUrl(List<ServiceNode> chain) {
        String targetUrl = FINAL_URL;
        for (int i = chain.size() - 1; i >= 0; i--) {
            ServiceNode currentNode = chain.get(i);
            String targetName = i == chain.size() - 1 ? "java" : chain.get(i + 1).getName();
            targetUrl = currentNode.getBaseUrl()
                    + "/chain?targetName=" + encode(targetName)
                    + "&targetUrl=" + encode(targetUrl);
        }
        return targetUrl;
    }

    /**
     * 构建链路日志展示名称。
     *
     * @param chain 服务链路
     * @return 链路名称
     */
    private String buildChainName(List<ServiceNode> chain) {
        List<String> nameList = CollUtil.newArrayList();
        for (ServiceNode serviceNode : chain) {
            nameList.add(serviceNode.getName());
        }
        nameList.add("java");
        return CollUtil.join(nameList, " -> ");
    }

    /**
     * 编码 URL 参数值。
     *
     * @param value 参数值
     * @return 编码后的参数值
     */
    private String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 编码不可用", e);
        }
    }

    private static class ServiceNode {

        private final String name;
        private final String baseUrl;

        private ServiceNode(String name, String baseUrl) {
            this.name = name;
            this.baseUrl = baseUrl;
        }

        public String getName() {
            return name;
        }

        public String getBaseUrl() {
            return baseUrl;
        }
    }

}
