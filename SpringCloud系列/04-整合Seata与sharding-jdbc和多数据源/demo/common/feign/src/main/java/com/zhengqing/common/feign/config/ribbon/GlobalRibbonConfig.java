//package com.zhengqing.common.feign.config.ribbon;
//
//import com.netflix.loadbalancer.IRule;
//import com.zhengqing.common.feign.config.ribbon.balancer.BalancerVersionRule;
//import com.zhengqing.common.feign.config.ribbon.balancer.BalancerWeightRule;
//import com.zhengqing.common.feign.enums.BalancerRuleTypeEnum;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
///**
// * <p> Ribbon配置类 </p>
// *
// * @author zhengqingya
// * @description
// * @date 2021/11/8 17:32
// */
//@Slf4j
//@Data
//@Configuration
//public class GlobalRibbonConfig {
//
//    @Value("${ribbon.rule-type:}")
//    private String ruleType;
//
//    @Bean
//    public IRule getRule() {
//        // 自定义负载均衡策略
//        BalancerRuleTypeEnum balancerRuleTypeEnum = BalancerRuleTypeEnum.getEnum(this.ruleType);
//        log.info("使用自定义负载均衡策略:[{}]", balancerRuleTypeEnum.getDesc());
//        switch (balancerRuleTypeEnum) {
//            case WEIGHT:
//                // 权重
//                return new BalancerWeightRule();
//            case VERSION:
//                // 同一集群优先带版本实例
//                return new BalancerVersionRule();
//            default:
//                // 默认权重
//                return new BalancerWeightRule();
//        }
//    }
//
//}
