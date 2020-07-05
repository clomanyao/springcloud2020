package com.cloneman.springcloud.controller;

import com.cloneman.springcloud.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 *#ClassName: PaymentHystrixController
 *#Author: lenovo
 *#Date: 2020/6/29 14:57
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/hystrix_ok/{id}")
    public String hystrix_ok(@PathVariable("id") Long id) {
       return paymentService.hystrix_ok(id);
    }

    @GetMapping("/hystrix_error/{id}")
    public String hystrix_error(@PathVariable("id") Long id) {
        return paymentService.hystrix_error(id);
    }

    @GetMapping("/circuitBreaker/{id}")
    public String circuitBreaker(@PathVariable("id") Long id){
        return paymentService.paymentCircuitBreaker(id);
    }
}
