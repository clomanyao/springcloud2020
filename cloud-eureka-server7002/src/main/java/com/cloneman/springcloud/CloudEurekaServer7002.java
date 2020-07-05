package com.cloneman.springcloud;/*
 *#ClassName: CloudEurekaServer7002
 *#Author: lenovo
 *#Date: 2020/6/27 23:08
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class CloudEurekaServer7002 {

    public static void main(String[] args) {
        SpringApplication.run(CloudEurekaServer7002.class,args);
    }

}
