package com.cloneman.springcloud.controller;
/*
 *#ClassName: StreamRabbitMQProviderController
 *#Author: lenovo
 *#Date: 2020/7/1 15:54
 */

import com.cloneman.springcloud.service.IMessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.Resources;

@RestController
public class StreamRabbitMQProviderController {

    @Resource
    private IMessageProvider iMessageProvider;

    @GetMapping("/send")
    public String send(){
        return iMessageProvider.send();
    }

}
