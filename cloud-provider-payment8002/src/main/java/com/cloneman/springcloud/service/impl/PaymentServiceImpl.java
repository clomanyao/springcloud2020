package com.cloneman.springcloud.service.impl;/*
 *#ClassName: PaymentServiceImpl
 *#Author: lenovo
 *#Date: 2020/6/27 18:19
 */

import com.cloneman.springcloud.dao.PaymentDao;
import com.cloneman.springcloud.entity.Payment;
import com.cloneman.springcloud.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentDao paymentDao;

    @Override
    public int insert(Payment payment) {
        return paymentDao.insert(payment);
    }

    @Override
    public Payment queryPaymentById(Long id) {
        return paymentDao.queryPaymentById(id);
    }
}
