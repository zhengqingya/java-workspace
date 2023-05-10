package com.zhengqing.common.feign.context;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * <p> 请求头上下文 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/6/30 9:24 下午
 */
@Slf4j
public class RequestHeaderHandler {

    public static final ThreadLocal<Map<String, String>> THREAD_LOCAL = new ThreadLocal<>();

    public static void setHeaderMap(Map<String, String> headerMap) {
        THREAD_LOCAL.set(headerMap);
    }

    public static Map<String, String> getHeaderMap() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }


}
