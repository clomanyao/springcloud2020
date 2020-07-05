package com.cloneman.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*
 *#ClassName: CloudAlibabaConsumerNacosOrder84
 *#Author: lenovo
 *#Date: 2020/7/5 15:39
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class CloudAlibabaConsumerNacosOrder84 {

    public static void main(String[] args) {
        SpringApplication.run(CloudAlibabaConsumerNacosOrder84.class,args);
    }
}
