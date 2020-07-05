package com.cloneman.springcloud.feign;

import com.cloneman.springcloud.entity.Payment;
import com.cloneman.springcloud.feign.impl.PaymentFeignServiceImpl;
import com.cloneman.springcloud.result.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "nacos-payment-provider",fallback = PaymentFeignServiceImpl.class)
public interface PaymentFeignService {

    @GetMapping(value = "/paymentSQL/{id}")
    public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id);

}
