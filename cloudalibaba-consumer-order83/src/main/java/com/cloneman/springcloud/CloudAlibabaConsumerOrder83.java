package com.cloneman.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/*
 *#ClassName: CloudAlibabaConsumerOrder83
 *#Author: lenovo
 *#Date: 2020/7/3 15:28
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CloudAlibabaConsumerOrder83 {

    public static void main(String[] args) {
        SpringApplication.run(CloudAlibabaConsumerOrder83.class,args);
    }
}
