package com.cloneman.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/*
 *#ClassName: NacosProviderController
 *#Author: lenovo
 *#Date: 2020/7/2 22:13
 */
@RestController
public class NacosProviderController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/nacos/{id}")
    public String nacos(@PathVariable("id") Long id){
        return "nacos"+serverPort+"  "+id;
    }
}
