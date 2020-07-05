package com.cloneman.springcloud.dao;

import com.cloneman.springcloud.entity.Payment;
import org.apache.ibatis.annotations.Param;

public interface PaymentDao {

    int insert(Payment payment);

    Payment queryPaymentById(@Param("id") Long id);
}
