package com.cloneman.springcloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
<<<<<<< HEAD
@MapperScan(basePackages = {"com.cloneman.com.cloneman.com.cloneman.springcloud.dao"})
=======
@MapperScan(basePackages = {"com.cloneman.com.cloneman.springcloud.dao"})
>>>>>>> 7a6e9c94be2a87ad850c8d0db9110d7bbed07393
public class CloudProviderPayment8002 {

    public static void main(String[] args) {
        SpringApplication.run(CloudProviderPayment8002.class,args);
    }
}
/*
 *#ClassName: CloudProviderPayment8002
 *#Author: lenovo
 *#Date: 2020/6/28 7:54
 */
