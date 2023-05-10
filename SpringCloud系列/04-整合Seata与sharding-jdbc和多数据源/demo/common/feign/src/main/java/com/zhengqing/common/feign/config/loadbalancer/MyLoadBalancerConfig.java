package com.zhengqing.common.feign.config.loadbalancer;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * <p> LoadBalancer自定义负载均衡器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/7/25 16:56
 */
@Configuration
@LoadBalancerClients(defaultConfiguration = {MyLoadBalancerConfig.class})
public class MyLoadBalancerConfig {

//    @Bean
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }

    @Bean
    ReactorLoadBalancer<ServiceInstance> customLoadBalancer(Environment environment,
                                                            LoadBalancerClientFactory loadBalancerClientFactory) {
        String serviceName = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new CustomInstanceLoadBalancer(loadBalancerClientFactory.getLazyProvider(serviceName, ServiceInstanceListSupplier.class), serviceName);
    }

}
