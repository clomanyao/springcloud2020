package com.cloneman.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/*
 *#ClassName: CloudAlibabaProviderPayment9003
 *#Author: lenovo
 *#Date: 2020/7/5 15:11
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CloudAlibabaProviderPayment9003 {
    public static void main(String[] args) {
        SpringApplication.run(CloudAlibabaProviderPayment9003.class,args);
    }
}
