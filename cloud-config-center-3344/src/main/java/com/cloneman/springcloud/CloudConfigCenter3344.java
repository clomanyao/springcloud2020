package com.cloneman.springcloud;/*
 *#ClassName: CloudConfigCenter3344
 *#Author: lenovo
 *#Date: 2020/6/30 22:13
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class CloudConfigCenter3344 {

    public static void main(String[] args) {
        SpringApplication.run(CloudConfigCenter3344.class,args);
    }
}
