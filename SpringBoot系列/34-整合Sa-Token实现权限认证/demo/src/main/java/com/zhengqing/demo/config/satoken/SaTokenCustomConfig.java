package com.zhengqing.demo.config.satoken;

import cn.dev33.satoken.strategy.SaStrategy;
import cn.dev33.satoken.util.SaFoxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * <p> 自定义sa-token生成策略 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/11/3 12:06
 */
@Configuration
public class SaTokenCustomConfig {

    /**
     * 重写 Sa-Token 框架内部算法策略
     */
    @Autowired
    public void rewriteSaStrategy() {
        // 重写 Token 生成策略
        SaStrategy.me.createToken = (loginId, loginType) -> {
            // 随机60位长度字符串
            return SaFoxUtil.getRandomString(60);
        };
    }

}
