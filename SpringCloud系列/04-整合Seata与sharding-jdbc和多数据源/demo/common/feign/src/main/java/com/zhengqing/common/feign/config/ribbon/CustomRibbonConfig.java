//package com.zhengqing.common.feign.config.ribbon;
//
//import org.springframework.cloud.netflix.ribbon.RibbonClients;
//import org.springframework.context.annotation.Configuration;
//
///**
// * <p> 启用自定义负载均衡策略 </p>
// *
// * @author zhengqingya
// * @description 可参考 https://cloud.spring.io/spring-cloud-static/Dalston.SR5/multi/multi_spring-cloud-ribbon.html#_customizing_the_ribbon_client
// * @date 2021/11/8 17:48
// */
//@Configuration
///*@RibbonClients(value = {
//        @RibbonClient(name="product", configuration = ProductConfiguration.class),
//        @RibbonClient(name = "customer", configuration = CustomerConfiguration.class)
//})*/
//// 使用全局的配置
//@RibbonClients(defaultConfiguration = GlobalRibbonConfig.class)
//public class CustomRibbonConfig {
//
//}
