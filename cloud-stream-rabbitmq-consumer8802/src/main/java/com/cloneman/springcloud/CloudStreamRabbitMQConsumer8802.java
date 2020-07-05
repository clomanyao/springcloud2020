package com.cloneman.springcloud;/*
 *#ClassName: CloudStreamRabbitMQConsumer8802
 *#Author: lenovo
 *#Date: 2020/7/1 16:26
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CloudStreamRabbitMQConsumer8802 {
    public static void main(String[] args) {
        SpringApplication.run(CloudStreamRabbitMQConsumer8802.class,args);
    }
}
