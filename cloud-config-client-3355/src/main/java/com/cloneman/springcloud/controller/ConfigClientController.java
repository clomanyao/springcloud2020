package com.cloneman.springcloud.controller;/*
 *#ClassName: ConfigClientController
 *#Author: lenovo
 *#Date: 2020/6/30 23:05
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class ConfigClientController {

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/config/info")
    public String configInfo(){
        return this.configInfo;
    }
}
