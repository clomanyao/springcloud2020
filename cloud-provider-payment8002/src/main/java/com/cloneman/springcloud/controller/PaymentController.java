package com.cloneman.springcloud.controller;/*
 *#ClassName: PaymentController
 *#Author: lenovo
 *#Date: 2020/6/27 18:22
 */

import com.cloneman.springcloud.entity.Payment;
import com.cloneman.springcloud.result.CommonResult;
import com.cloneman.springcloud.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Value("${server.port}")
    private Integer serverport;

    @PostMapping("/save")
    /*
    * 切记:但使用springcloud微服务之间传参的时候,当使用restTemplate 模板时，
    * springcloud是以json串模式在微服务之间传参,必须加入@RequestBody
    * */
    public CommonResult<Payment> insert(@RequestBody Payment payment){
<<<<<<< HEAD
        return paymentService.insert(payment)==1? CommonResult.okOf():CommonResult.errorOf();
=======
        return paymentService.insert(payment)==1?CommonResult.okOf():CommonResult.errorOf();
>>>>>>> 7a6e9c94be2a87ad850c8d0db9110d7bbed07393
    }


    @GetMapping("/get/{id}")
    public CommonResult<Payment> queryPaymentById(@PathVariable("id") Long id){
        Payment payment = paymentService.queryPaymentById(id);
        payment.setSerial(payment.getSerial()+serverport);
        return CommonResult.okOf(payment);
    }

    @GetMapping("/lb")
    public String lb(){
        return String.valueOf(serverport);
    }
}
