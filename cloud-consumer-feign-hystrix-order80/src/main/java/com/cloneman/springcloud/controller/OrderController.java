package com.cloneman.springcloud.controller;

import com.cloneman.springcloud.feign.PaymentFeignService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 *#ClassName: PaymentController
 *#Author: lenovo
 *#Date: 2020/6/29 15:28
 */
@RestController
@RequestMapping("/consumer/order")
@DefaultProperties(defaultFallback = "hystrix_global_error_handle")
public class OrderController {

    @Autowired
    private PaymentFeignService paymentFeignService;

    @GetMapping("/hystrix_ok/{id}")
    @HystrixCommand
    public String hystrix_ok(@PathVariable("id") Long id) {
        int a=1/0;
        return paymentFeignService.hystrix_ok(id);
    }

    @GetMapping("/hystrix_error/{id}")
    @HystrixCommand(
            fallbackMethod = "hystrix_error_handle",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
            }
    )
    public String hystrix_error(@PathVariable("id") Long id) {
        int a=1/0;
        return paymentFeignService.hystrix_error(id);
    }

    public String hystrix_error_handle(@PathVariable("id") Long id) {
        return "服务超时或者服务异常，请稍后再试！";
    }

    public String hystrix_global_error_handle() {
        return "全局 服务超时或者服务异常，请稍后再试！";
    }
}
