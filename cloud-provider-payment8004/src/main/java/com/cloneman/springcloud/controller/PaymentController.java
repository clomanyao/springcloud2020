package com.cloneman.springcloud.controller;
/*
 *#ClassName: PaymentController
 *#Author: lenovo
 *#Date: 2020/6/28 14:08
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PaymentController {

    @Value("${server.port}")
    private Integer port;

    @GetMapping("/payment")
    public String paymentZk(){
        return "zookeeper"+port;
    }
}
