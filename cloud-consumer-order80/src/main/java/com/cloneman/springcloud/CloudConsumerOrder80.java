package com.cloneman.springcloud;/*
 *#ClassName: CloudConsumerOrder80
 *#Author: lenovo
 *#Date: 2020/6/27 20:27
 */

import com.cloneman.myIRule.MyIRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient //开启服务发现
//@RibbonClient(name = "CLOUD-PAYMENT-SERVICE",configuration = {MyIRule.class})
//@RibbonClient(name = "cloud-payment-service",configuration = {MyIRule.class})
public class CloudConsumerOrder80 {
    public static void main(String[] args) {
        SpringApplication.run(CloudConsumerOrder80.class, args);
    }
}
