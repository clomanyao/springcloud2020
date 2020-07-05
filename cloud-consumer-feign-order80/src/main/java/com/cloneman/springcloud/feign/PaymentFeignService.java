package com.cloneman.springcloud.feign;
/*
 *#ClassName: PaymentFeignService
 *#Author: lenovo
 *#Date: 2020/6/29 12:47
 */

import com.cloneman.springcloud.entity.Payment;
import com.cloneman.springcloud.result.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("cloud-payment-service")
public interface PaymentFeignService {

    @GetMapping("/payment/get/{id}")
    CommonResult<Payment> queryPaymentById(@PathVariable("id") Long id);

    @GetMapping("/payment/feign/timeout")
    String feignTimeOut();
}
