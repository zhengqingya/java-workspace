//package com.zhengqing.common.feign.util;
//
//import com.alibaba.nacos.api.naming.pojo.Instance;
//import com.zhengqing.common.feign.enums.BalancerRuleTypeEnum;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * <p> 自定义负载均衡策略-nacos实例操作工具类 </p>
// *
// * @author zhengqingya
// * @description
// * @date 2021/11/18 8:37 下午
// */
//@Slf4j
//public class BalancerInstanceUtil {
//
//    public static void printInstance(BalancerRuleTypeEnum ruleTypeEnum, Instance instance) {
//        if (instance == null) {
//            return;
//        }
//        log.info("自定义负载均衡策略-[{}] serviceName: [{}], clusterName: [{}], ip: [{}] port: [{}]",
//                ruleTypeEnum.getDesc(),
//                instance.getServiceName(),
//                instance.getClusterName(),
//                instance.getIp(),
//                instance.getPort()
//        );
//    }
//
//}
