package com.zhengqing.demo.constant;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * <p> 全局常用变量 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/6/25 10:25
 */
public interface AppConstant {

    /**
     * 开放url
     */
    List<String> OPEN_URL_LIST = Lists.newArrayList("/doc.html/**", "/swagger-resources/**");

}
