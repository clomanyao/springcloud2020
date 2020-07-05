package com.cloneman.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/*
 *#ClassName: CloudAlibabaProvierPayment9001
 *#Author: lenovo
 *#Date: 2020/7/2 22:06
 */
@SpringBootApplication
@EnableDiscoveryClient  //开启服务
public class CloudAlibabaProvierPayment9002 {
    public static void main(String[] args) {
        SpringApplication.run(CloudAlibabaProvierPayment9002.class,args);
    }
}
