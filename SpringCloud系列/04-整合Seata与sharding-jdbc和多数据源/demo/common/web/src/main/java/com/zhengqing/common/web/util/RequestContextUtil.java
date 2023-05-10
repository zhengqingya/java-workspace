package com.zhengqing.common.web.util;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

/**
 * <p> 上下文请求信息工具类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/6/30 9:38 下午
 */
@Slf4j
public class RequestContextUtil {

    /**
     * 获取请求头数据
     *
     * @return key->请求头名称 value->请求头值
     * @author zhengqingya
     * @date 2021/6/30 9:39 下午
     */
    public static Map<String, String> getHeaderMap() {
        Map<String, String> headerMap = Maps.newLinkedHashMap();
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                return headerMap;
            }
            HttpServletRequest request = requestAttributes.getRequest();
            Enumeration<String> enumeration = request.getHeaderNames();
            while (enumeration.hasMoreElements()) {
                String key = enumeration.nextElement();
                String value = request.getHeader(key);
                headerMap.put(key, value);
            }
        } catch (Exception e) {
            log.error("《RequestContextUtil》 获取请求头参数失败：", e);
        }
        return headerMap;
    }

}
