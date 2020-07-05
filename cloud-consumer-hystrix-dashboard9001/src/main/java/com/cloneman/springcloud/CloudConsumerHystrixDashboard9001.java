package com.cloneman.springcloud;/*
 *#ClassName: CloudConsumerHystrixDashboard9001
 *#Author: lenovo
 *#Date: 2020/6/30 16:05
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
public class CloudConsumerHystrixDashboard9001 {

    public static void main(String[] args) {
        SpringApplication.run(CloudConsumerHystrixDashboard9001.class,args);
    }

}
