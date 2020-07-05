package com.cloneman.springcloud;/*
 *#ClassName: CloudConfigClient3355
 *#Author: lenovo
 *#Date: 2020/6/30 22:57
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CloudConfigClient3355 {

    public static void main(String[] args) {
        SpringApplication.run(CloudConfigClient3355.class,args);
    }
}
