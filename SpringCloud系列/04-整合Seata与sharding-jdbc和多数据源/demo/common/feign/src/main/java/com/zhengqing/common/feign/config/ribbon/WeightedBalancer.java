//package com.zhengqing.common.feign.config.ribbon;
//
//import com.alibaba.nacos.api.naming.pojo.Instance;
//import com.alibaba.nacos.client.naming.core.Balancer;
//
//import java.util.List;
//
///**
// * <p> 根据随机权重策略, 从一群服务器中选择一个实例 </p>
// *
// * @author zhengqingya
// * @description
// * @date 2021/11/8 18:00
// */
//public class WeightedBalancer extends Balancer {
//
//    public static Instance chooseInstanceByRandomWeight(List<Instance> instanceList) {
//        // 这是父类Balancer自带的根据随机权重获取服务的方法.
//        return getHostByRandomWeight(instanceList);
//    }
//
//}
