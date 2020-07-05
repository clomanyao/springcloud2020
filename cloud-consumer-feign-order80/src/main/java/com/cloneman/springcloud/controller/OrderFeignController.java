package com.cloneman.springcloud.controller;

import com.cloneman.springcloud.entity.Payment;
import com.cloneman.springcloud.feign.PaymentFeignService;
import com.cloneman.springcloud.result.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 *#ClassName: OrderFeignController
 *#Author: lenovo
 *#Date: 2020/6/29 13:00
 */
@RestController
@RequestMapping("/consumer/order")
public class OrderFeignController {

    @Autowired
    private PaymentFeignService paymentFeignService;


    @GetMapping("/get/{id}")
    public CommonResult<Payment> queryPaymentById(@PathVariable("id") Long id){
        return paymentFeignService.queryPaymentById(id);
    }

    @GetMapping("/feign/timeout")
    public String feignTimeOut(){
        return paymentFeignService.feignTimeOut();
    }

}
