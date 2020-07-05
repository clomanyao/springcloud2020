package com.cloneman.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public interface LoadBalanced {

    ServiceInstance getInstance(List<ServiceInstance> instances);
}
