package com.cloneman.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*
 *#ClassName: CloudConsumerFeignHystrixOrder80
 *#Author: lenovo
 *#Date: 2020/6/29 15:22
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableHystrix  //开启Hystrix功能
public class CloudConsumerFeignHystrixOrder80 {

    public static void main(String[] args) {
        SpringApplication.run(CloudConsumerFeignHystrixOrder80.class,args);
    }
}
