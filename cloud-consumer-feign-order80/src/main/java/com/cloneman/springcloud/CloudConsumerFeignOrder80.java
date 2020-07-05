package com.cloneman.springcloud;/*
 *#ClassName: CloudConsumerFeginOrder80
 *#Author: lenovo
 *#Date: 2020/6/29 12:46
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages= {"com.cloneman.springcloud.feign"})
@EnableEurekaClient
public class CloudConsumerFeignOrder80 {

    public static void main(String[] args) {
        SpringApplication.run(CloudConsumerFeignOrder80.class,args);
    }
}
