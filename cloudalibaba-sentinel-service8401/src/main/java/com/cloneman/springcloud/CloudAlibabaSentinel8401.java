package com.cloneman.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/*
 *#ClassName: CloudAlibabaSentine8401
 *#Author: lenovo
 *#Date: 2020/7/4 11:20
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CloudAlibabaSentinel8401 {

    public static void main(String[] args) {
        SpringApplication.run(CloudAlibabaSentinel8401.class,args);
    }
}
