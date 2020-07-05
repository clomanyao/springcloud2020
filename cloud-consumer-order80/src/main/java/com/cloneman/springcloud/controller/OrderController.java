package com.cloneman.springcloud.controller;
/*
 *#ClassName: OrderController
 *#Author: lenovo
 *#Date: 2020/6/27 20:30
 */

import com.cloneman.springcloud.entity.Payment;
import com.cloneman.springcloud.lb.LoadBalanced;
import com.cloneman.springcloud.result.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/consumer/order")
public class OrderController {
    private static final Logger LOGGER=LoggerFactory.getLogger(OrderController.class);

    //private static final String PAYMENT_URL="http://127.0.0.1:8001/payment";
    private static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE/payment";
    //private static final String PAYMENT_URL = "http://cloud-payment-service/payment";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private LoadBalanced loadBalanced;

    @GetMapping("/savePayment")
    public CommonResult<Payment> savePayment(Payment payment) {
        return restTemplate.postForObject(PAYMENT_URL + "/save", payment, CommonResult.class);
    }

    @GetMapping("/getPayment/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) {
        return restTemplate.getForObject(PAYMENT_URL + "/get/" + id, CommonResult.class);
    }

    @GetMapping("/getPayment2/{id}")
    public CommonResult<Payment> getPaymentById2(@PathVariable("id") Long id) {
        ResponseEntity<CommonResult> entity = restTemplate.getForEntity(PAYMENT_URL + "/get/" + id, CommonResult.class);
        if(entity.getStatusCode().is2xxSuccessful()){
            return entity.getBody();
        }else {
            return CommonResult.errorOf();
        }
    }

    @GetMapping("/discovery")
    public Object discovery(){
        List<String> services = discoveryClient.getServices();
        for(String service:services){
            LOGGER.info("所有的服务:"+service);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-service");
        for (ServiceInstance instance : instances) {
            LOGGER.info("服务信息:"+instance.getInstanceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
        }
        return discoveryClient;
    }

    @GetMapping("/lb")
    public String lb(){
        ServiceInstance instance = loadBalanced.getInstance(discoveryClient.getInstances("cloud-payment-service"));
        URI uri = instance.getUri();
        String serverPort = restTemplate.getForObject(uri + "/payment/lb", String.class);
        return serverPort;
    }
}
