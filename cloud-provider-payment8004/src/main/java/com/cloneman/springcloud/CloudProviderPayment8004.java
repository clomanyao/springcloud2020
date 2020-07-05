package com.cloneman.springcloud;/*
 *#ClassName: CloudProviderPayment8004
 *#Author: lenovo
 *#Date: 2020/6/28 13:56
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  //开启服务发现
public class CloudProviderPayment8004 {

    public static void main(String[] args) {
        SpringApplication.run(CloudProviderPayment8004.class,args);
    }
}
