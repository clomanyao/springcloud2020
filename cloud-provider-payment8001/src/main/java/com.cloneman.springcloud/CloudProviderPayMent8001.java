package com.cloneman.springcloud;
/*
 *#ClassName: CloudProviderPayMent8001
 *#Author: lenovo
 *#Date: 2020/6/19 23:20
 */

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
<<<<<<< HEAD
@MapperScan(basePackages = {"com.cloneman.com.cloneman.com.cloneman.springcloud.dao"})
=======
@MapperScan(basePackages = {"com.cloneman.com.cloneman.springcloud.dao"})
>>>>>>> 7a6e9c94be2a87ad850c8d0db9110d7bbed07393
@EnableEurekaClient
public class CloudProviderPayMent8001 {
    public static void main(String[] args) {
        SpringApplication.run(CloudProviderPayMent8001.class,args);
    }
}
