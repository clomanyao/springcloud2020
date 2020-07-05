package com.cloneman.springcloud;/*
 *#ClassName: CloudConsumerZkOrder80
 *#Author: lenovo
 *#Date: 2020/6/28 14:41
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CloudConsumerZkOrder80 {
    public static void main(String[] args) {
        SpringApplication.run(CloudConsumerZkOrder80.class,args);
    }
}
