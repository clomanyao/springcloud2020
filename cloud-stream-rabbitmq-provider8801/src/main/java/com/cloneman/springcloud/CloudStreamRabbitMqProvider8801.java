package com.cloneman.springcloud;/*
 *#ClassName: CloudStreamRabbitMqProvider8801
 *#Author: lenovo
 *#Date: 2020/7/1 13:46
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CloudStreamRabbitMqProvider8801 {

    public static void main(String[] args) {
        SpringApplication.run(CloudStreamRabbitMqProvider8801.class,args);
    }
}
