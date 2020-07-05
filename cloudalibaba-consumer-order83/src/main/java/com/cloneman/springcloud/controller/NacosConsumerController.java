package com.cloneman.springcloud.controller;/*
 *#ClassName: NacosConsumerController
 *#Author: lenovo
 *#Date: 2020/7/3 15:33
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/consumer")
public class NacosConsumerController {

    @Value("${spring-nacos.provider-url}")
    public String PAYMENTURL;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/payment/{id}")
    public String consumer(@PathVariable("id") Long id){
        return restTemplate.getForObject(PAYMENTURL+"/nacos/"+id,String.class);
    }
}
