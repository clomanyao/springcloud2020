package com.cloneman.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/*
 *#ClassName: CloudAlibabaConfigCenter3377
 *#Author: lenovo
 *#Date: 2020/7/3 17:10
 */
@SpringBootApplication
public class CloudAlibabaConfigCenter3377 {

    public static void main(String[] args) {
        SpringApplication.run(CloudAlibabaConfigCenter3377.class,args);
    }
}
