package com.cloneman.springcloud;/*
 *#ClassName: CloudEurekaServer7001
 *#Author: lenovo
 *#Date: 2020/6/27 22:00
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class CloudEurekaServer7001 {
    public static void main(String[] args) {
        SpringApplication.run(CloudEurekaServer7001.class,args);
    }
}
