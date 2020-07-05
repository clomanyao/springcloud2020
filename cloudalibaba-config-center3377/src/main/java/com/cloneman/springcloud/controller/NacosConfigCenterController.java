package com.cloneman.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 *#ClassName: NacosConfigCenterController
 *#Author: lenovo
 *#Date: 2020/7/3 17:17
 */
@RestController
//@RefreshScope
public class NacosConfigCenterController {

        @Value("${config.info}")
        private String configInfo;

        @GetMapping("/configInfo")
        public String configInfo(){
            return this.configInfo;
        }

}
