package com.cloneman.springcloud.service;
/*
 *#ClassName: PaymentService
 *#Author: lenovo
 *#Date: 2020/6/29 14:54
 */


import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class PaymentService {

    public String hystrix_ok(Long id) {
        return "hystrix" + Thread.currentThread().getName() + "ok" + id;
    }

    public String hystrix_error(Long id) {
        //int a=1/0;
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hystrix" + Thread.currentThread().getName() + "error" + id;
    }

    //=============服务熔断========================//
    @HystrixCommand(
            fallbackMethod = "paymentCircuitBreaker_fallback",
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),    //是否开启断路器
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),     //请求次数
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),    //时间范围
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),  //失败率达到多少后跳闸
            })
    public String paymentCircuitBreaker(Long id) {
        if (id < 0) {
            throw new RuntimeException(+id + ":" + "*****id 不能负数");
        }
        String serialNumber = IdUtil.simpleUUID();

        return Thread.currentThread().getName() + "\t" + "调用成功,流水号：" + serialNumber;
    }

    public String paymentCircuitBreaker_fallback(Long id) {
        return "id:"+id+" 不能为负数,请稍后才尝试";
    }
}
