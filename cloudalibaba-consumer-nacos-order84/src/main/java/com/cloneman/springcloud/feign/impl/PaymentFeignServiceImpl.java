package com.cloneman.springcloud.feign.impl;/*
 *#ClassName: PaymentFeignServiceImpl
 *#Author: lenovo
 *#Date: 2020/7/5 17:03
 */

import com.cloneman.springcloud.entity.Payment;
import com.cloneman.springcloud.feign.PaymentFeignService;
import com.cloneman.springcloud.result.CommonResult;
import org.springframework.stereotype.Component;

@Component
public class PaymentFeignServiceImpl implements PaymentFeignService {

    @Override
    public CommonResult<Payment> paymentSQL(Long id) {
        return CommonResult.errorOf();
    }
}
