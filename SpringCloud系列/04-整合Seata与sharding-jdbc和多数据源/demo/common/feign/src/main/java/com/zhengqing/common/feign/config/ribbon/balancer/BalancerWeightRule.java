//package com.zhengqing.common.feign.config.ribbon.balancer;
//
//import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
//import com.alibaba.cloud.nacos.NacosServiceManager;
//import com.alibaba.cloud.nacos.ribbon.NacosServer;
//import com.alibaba.nacos.api.exception.NacosException;
//import com.alibaba.nacos.api.naming.NamingService;
//import com.alibaba.nacos.api.naming.pojo.Instance;
//import com.netflix.client.config.IClientConfig;
//import com.netflix.loadbalancer.AbstractLoadBalancerRule;
//import com.netflix.loadbalancer.BaseLoadBalancer;
//import com.netflix.loadbalancer.Server;
//import com.zhengqing.common.feign.enums.BalancerRuleTypeEnum;
//import com.zhengqing.common.feign.util.BalancerInstanceUtil;
//import lombok.extern.slf4j.Slf4j;
//
//import javax.annotation.Resource;
//
///**
// * <p> 自定义负载均衡策略-权重 </p>
// *
// * @author zhengqingya
// * @description
// * @date 2021/11/18 8:37 下午
// */
//@Slf4j
//public class BalancerWeightRule extends AbstractLoadBalancerRule {
//
//    @Resource
//    private NacosServiceManager nacosServiceManager;
//
//    @Resource
//    private NacosDiscoveryProperties nacosDiscoveryProperties;
//
//    @Override
//    public void initWithNiwsConfig(IClientConfig iClientConfig) {
//        // 读取配置文件, 并且初始化, ribbon内部基本上用不上
//    }
//
//    /**
//     * 这个方法是实现负载均衡策略的方法
//     */
//    @Override
//    public Server choose(Object key) {
//        try {
//            // 1、获取当前服务的分组名称
//            String groupName = this.nacosDiscoveryProperties.getGroup();
//            // 2、获取当前服务的负载均衡器
//            BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
//            // 3、获取目标服务的服务名
//            String serviceName = baseLoadBalancer.getName();
//            // 4、获取nacos提供的服务注册api
//            NamingService namingService = this.nacosServiceManager.getNamingService(this.nacosDiscoveryProperties.getNacosProperties());
//            // 5、根据目标服务名称和分组名称去获取服务实例，nacos实现了权重的负载均衡算法  false: 及时获取nacos注册服务信息
//            Instance toBeChooseInstance = namingService.selectOneHealthyInstance(serviceName, groupName, false);
//            BalancerInstanceUtil.printInstance(BalancerRuleTypeEnum.WEIGHT, toBeChooseInstance);
//            return new NacosServer(toBeChooseInstance);
//        } catch (NacosException e) {
//            log.error("自定义负载均衡策略-权重 调用异常: ", e);
//            return null;
//        }
//    }
//
//}
