package com.zhengqing.common.base.context;

import lombok.extern.slf4j.Slf4j;

/**
 * <p> 租户ID上下文 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/6/30 9:24 下午
 */
@Slf4j
public class TenantIdContext {

    /**
     * 租户ID
     */
    public static final ThreadLocal<Integer> TENANT_ID_THREAD_LOCAL = new ThreadLocal<>();
    /**
     * 租户ID是否启用标识
     * true : 是 -> 执行sql时，自动拼接租户ID
     * false: 否 -> 执行sql时，不自动拼接租户ID
     */
    public static final ThreadLocal<Boolean> TENANT_ID_FLAG_THREAD_LOCAL = new ThreadLocal<>();

    public static void setTenantId(Integer tenantId) {
        TENANT_ID_THREAD_LOCAL.set(tenantId);
        TENANT_ID_FLAG_THREAD_LOCAL.set(true);
    }

    public static Integer getTenantId() {
        return TENANT_ID_THREAD_LOCAL.get();
    }

    public static void removeFlag() {
        TENANT_ID_FLAG_THREAD_LOCAL.set(false);
    }


    public static Boolean getFlag() {
        return TENANT_ID_FLAG_THREAD_LOCAL.get();
    }

    public static void remove() {
        TENANT_ID_THREAD_LOCAL.remove();
        TENANT_ID_FLAG_THREAD_LOCAL.remove();
    }

}
