package com.zhengqing.demo.modules.weixin.service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>  微信 - 消息处理 - 服务类 </p>
 *
 * @author: zhengqing
 * @date: 2019-09-18 10:51:57
 */
public interface IMsgService {

    /**
     * 消息处理
     *
     * @param request:
     * @return: java.lang.String
     */
    String processRequest(HttpServletRequest request);

    /**
     * 原样返回消息内容
     *
     * @param request:
     * @return: java.lang.String
     */
    String processRequestReturnSameMsg(HttpServletRequest request);

}
