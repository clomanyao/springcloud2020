package com.cloneman.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/*
 *#ClassName: CloudConsumerConsulOrder80
 *#Author: lenovo
 *#Date: 2020/6/28 15:52
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CloudConsumerConsulOrder80 {

    public static void main(String[] args) {
        SpringApplication.run(CloudConsumerConsulOrder80.class,args);
    }
}
