package com.zhengqing.mybatis.plugin;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * <p> 拦截器-责任链 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/26 02:33
 */
public class InterceptorChain {

    List<Interceptor> interceptorList = Lists.newArrayList();

    public Object pluginAll(Object target) {
        for (Interceptor interceptor : this.interceptorList) {
            target = interceptor.plugin(target);
        }
        return target;
    }

    public void addInterceptor(Interceptor interceptor) {
        this.interceptorList.add(interceptor);
    }

    public List<Interceptor> getInterceptorList() {
        return Collections.unmodifiableList(this.interceptorList);
    }

}
