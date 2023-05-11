package com.zhengqing.common.feign.config.loadbalancer;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.google.common.collect.Lists;
import com.zhengqing.common.feign.enums.BalancerRuleTypeEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p> 自定义负载均衡策略 </p>
 *
 * @author zhengqingya
 * @description {@link  org.springframework.cloud.loadbalancer.core.RandomLoadBalancer}
 * @date 2022/7/25 17:04
 */
@Slf4j
public class CustomInstanceLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    /**
     * nacos服务配置信息
     */
    @Resource
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    /**
     * loadbalancer 提供的访问目标服务的名称
     */
    final String serviceName;

    /**
     * loadbalancer 提供访问的服务列表
     */
    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    /**
     * 负载均衡策略
     * {@link com.zhengqing.common.feign.enums.BalancerRuleTypeEnum }
     */
    @Value("${feign.rule-type:}")
    private String ruleType;

    public CustomInstanceLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceName) {
        this.serviceName = serviceName;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = this.serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map((serviceInstances) -> this.processInstanceResponse(supplier, serviceInstances));
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier, List<ServiceInstance> serviceInstances) {
        Response<ServiceInstance> serviceInstanceResponse = this.getInstanceResponse(serviceInstances);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    /**
     * 对负载均衡的服务进行筛选
     *
     * @param instanceList loadbalancer可访问目标服务的实例 (只会提供健康的实例，无需担心无法访问的情况)
     * @return 指定实例
     * @author zhengqingya
     * @date 2022/7/25 17:43
     */
    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instanceList) {
        if (instanceList.isEmpty()) {
            log.warn("No servers available for service: " + this.serviceName);
            return new EmptyResponse();
        }
        ServiceInstance targetInstance;
        BalancerRuleTypeEnum balancerRuleTypeEnum = BalancerRuleTypeEnum.getEnum(this.ruleType);
        switch (balancerRuleTypeEnum) {
            case VERSION:
                // 同一集群优先带版本实例
                targetInstance = this.getInstanceForVersion(instanceList);
            default:
                // 默认权重
                targetInstance = getInstanceForRandomWeight(instanceList);
        }
        log.info("自定义负载均衡 目标instanceId: [{}]", targetInstance.getInstanceId());
        return new DefaultResponse(targetInstance);
    }

    /**
     * 随机权重策略
     *
     * @param instanceList loadbalancer可访问目标服务的实例 (只会提供健康的实例，无需担心无法访问的情况)
     * @return 指定实例
     * @author zhengqingya
     * @date 2022/7/25 8:32 下午
     */
    public static ServiceInstance getInstanceForRandomWeight(List<ServiceInstance> instanceList) {
        int index = ThreadLocalRandom.current().nextInt(instanceList.size());
        return instanceList.get(index);
    }

    /**
     * 同一集群优先带版本策略
     *
     * @param instanceList loadbalancer可访问目标服务的实例 (只会提供健康的实例，无需担心无法访问的情况)
     * @return 指定实例
     * @author zhengqingya
     * @date 2022/7/25 17:43
     */
    @SneakyThrows(Exception.class)
    private ServiceInstance getInstanceForVersion(List<ServiceInstance> instanceList) {
        // 1、获取当前服务的分组名称、集群名称、版本号
        String clusterName = this.nacosDiscoveryProperties.getClusterName();
        String version = this.nacosDiscoveryProperties.getMetadata().get("version");

        // 2、划分不同类型的实例数据
        // 同一集群的服务实例
        List<ServiceInstance> sameClusterInstanceList = Lists.newLinkedList();
        // 同一集群&同一版本的服务实例
        List<ServiceInstance> sameVersionInstanceList = Lists.newLinkedList();
        instanceList.forEach(targetInstanceItem -> {
            Map<String, String> targetInstanceMetadata = targetInstanceItem.getMetadata();
            String targetCluster = targetInstanceMetadata.get("nacos.cluster");
            if (clusterName.equals(targetCluster)) {
                sameClusterInstanceList.add(targetInstanceItem);
                String targetVersion = targetInstanceMetadata.get("version");
                if (version.equals(targetVersion)) {
                    sameVersionInstanceList.add(targetInstanceItem);
                }
            }
        });

        // 3、选择合适的服务实例
        ServiceInstance targetInstance;
        if (CollectionUtils.isEmpty(sameClusterInstanceList) || CollectionUtils.isEmpty(sameVersionInstanceList)) {
            targetInstance = getInstanceForRandomWeight(sameClusterInstanceList);
        } else {
            targetInstance = getInstanceForRandomWeight(sameVersionInstanceList);
        }
        return targetInstance;
    }

}

