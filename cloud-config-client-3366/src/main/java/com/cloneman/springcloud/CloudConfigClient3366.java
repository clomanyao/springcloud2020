package com.cloneman.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/*
 *#ClassName: CloudConfigClient3366
 *#Author: lenovo
 *#Date: 2020/7/1 10:32
 */
@SpringBootApplication
@EnableEurekaClient
public class CloudConfigClient3366 {
    public static void main(String[] args) {
        SpringApplication.run(CloudConfigClient3366.class,args);
    }
}
