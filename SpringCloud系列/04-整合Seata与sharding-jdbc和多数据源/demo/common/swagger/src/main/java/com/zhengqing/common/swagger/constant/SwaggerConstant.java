package com.zhengqing.common.swagger.constant;

/**
 * <p> 全局常用变量 - swagger </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/6/1 17:08
 */
public interface SwaggerConstant {

    /**
     * 当前页、每页显示数量 {@link com.zhengqing.common.base.constant.BaseConstant#PAGE_NUM}
     */
    String PAGE_NUM = "pageNum";
    String PAGE_SIZE = "pageSize";

    /**
     * 认证请求头
     */
    String REQUEST_HEADER_AUTHORIZATION = "Authorization-smallboot";

    /**
     * 租户ID
     */
    String TENANT_ID = "TENANT_ID";

    /**
     * 网关服务端口
     */
    String GATEWAY_PORT = "1218";

    /**
     * 授权服务端口
     */
    String AUTH_PORT = "1219";

}
