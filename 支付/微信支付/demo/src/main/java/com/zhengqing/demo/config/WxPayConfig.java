package com.zhengqing.demo.config;

import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> 微信支付配置 -- 单商户模式 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/8/19 9:07
 */
@Configuration
@AllArgsConstructor
@ConditionalOnClass(WxPayService.class)
@EnableConfigurationProperties(WxPayProperty.class)
public class WxPayConfig {

    private WxPayProperty wxPayProperty;

    @Bean
    @ConditionalOnMissingBean
    public WxPayService wxPayService() {
        com.github.binarywang.wxpay.config.WxPayConfig payConfig = new com.github.binarywang.wxpay.config.WxPayConfig();
        payConfig.setAppId(StringUtils.trimToNull(this.wxPayProperty.getAppId()));
        payConfig.setMchId(StringUtils.trimToNull(this.wxPayProperty.getMchId()));
        payConfig.setMchKey(StringUtils.trimToNull(this.wxPayProperty.getMchKey()));
        payConfig.setSubAppId(StringUtils.trimToNull(this.wxPayProperty.getSubAppId()));
        payConfig.setSubMchId(StringUtils.trimToNull(this.wxPayProperty.getSubMchId()));
        payConfig.setKeyPath(StringUtils.trimToNull(this.wxPayProperty.getKeyPath()));

        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

}
