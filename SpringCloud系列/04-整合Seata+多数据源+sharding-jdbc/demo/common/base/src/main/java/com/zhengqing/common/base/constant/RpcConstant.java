package com.zhengqing.common.base.constant;


import com.google.common.collect.Lists;

import java.util.List;

/**
 * <p> RPC常用变量 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/7/20 18:16
 */
public interface RpcConstant extends BaseConstant {

    // ===============================================================================
    // ============================ ↓↓↓↓↓↓ rpc ↓↓↓↓↓↓ ================================
    // ===============================================================================

    String RPC_BASE_PACKAGE = "com.zhengqing";

    /**
     * 服务
     */
    String RPC_DEMO = "demo";
    String RPC_SYSTEM = "system";
    String RPC_UMS = "ums";
    String RPC_PAY = "pay";
    String RPC_TOOL = "tool";
    String RPC_BASIC = "basic";
    List<String> ALL_RPC_SERVICE_NAME_LIST = Lists.newArrayList(
            RPC_DEMO, RPC_SYSTEM, RPC_TOOL
    );

    /**
     * rpc-api前缀
     */
    String RPC_API_PREFIX = "/rpc/client";
    String RPC_API_PREFIX_WEB = RPC_API_PREFIX + "/web";
    String RPC_API_PREFIX_SYSTEM = RPC_API_PREFIX + "/system";
    String RPC_API_PREFIX_UMS = RPC_API_PREFIX + "/ums";
    String RPC_API_PREFIX_PAY = RPC_API_PREFIX + "/pay";

    /**
     * rpc服务调用不需要封装返回值的api
     * 　　 ?　 =>　 匹配一个字符
     * 　　 *　 =>  匹配0个及以上字符
     * 　　 **　=>  匹配0个及以上目录
     */
    List<String> RETURN_VALUE_HANDLER_EXCLUDE_API_LIST = Lists.newArrayList(
            "*:/rpc/client/**/*",
            "POST:/oauth/token",
            "POST:/auth/oauth/token"
    );

    // ===============================================================================
    // ============================ ↓↓↓↓↓↓ other ↓↓↓↓↓↓ ==============================
    // ===============================================================================


}
