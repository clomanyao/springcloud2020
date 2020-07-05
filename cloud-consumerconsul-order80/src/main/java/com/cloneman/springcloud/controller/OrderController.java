package com.cloneman.springcloud.controller;/*
 *#ClassName: OrderController
 *#Author: lenovo
 *#Date: 2020/6/28 15:56
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/consumer/order")
public class OrderController {

    private static final String PAYMENT_URL="http://cloud-payment-service/";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/payment")
    public String payment(){
        return restTemplate.getForObject(PAYMENT_URL+"payment",String.class);
    }
}
