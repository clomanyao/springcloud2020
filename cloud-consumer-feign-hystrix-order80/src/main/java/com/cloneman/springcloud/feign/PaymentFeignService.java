package com.cloneman.springcloud.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("cloud-payment-hystrix-service")
public interface PaymentFeignService {

    @GetMapping("/payment/hystrix_ok/{id}")
    public String hystrix_ok(@PathVariable("id") Long id);

    @GetMapping("/payment/hystrix_error/{id}")
    public String hystrix_error(@PathVariable("id") Long id);
}
