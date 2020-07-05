package com.cloneman.springcloud;/*
 *#ClassName: CloudProviderConsulPayment8006
 *#Author: lenovo
 *#Date: 2020/6/28 15:24
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  //服务发现
public class CloudProviderConsulPayment8006 {

    public static void main(String[] args) {
        SpringApplication.run(CloudProviderConsulPayment8006.class,args);
    }

}
