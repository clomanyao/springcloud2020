package com.cloneman.springcloud.service;/*
 *#ClassName: PaymentService
 *#Author: lenovo
 *#Date: 2020/6/27 18:19
 */

import com.cloneman.springcloud.entity.Payment;

public interface PaymentService {

    int insert(Payment payment);

    Payment queryPaymentById(Long id);
}
