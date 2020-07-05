package com.cloneman.springcloud.config;/*
 *#ClassName: FeignLogLevelConfig
 *#Author: lenovo
 *#Date: 2020/6/29 13:44
 */

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignLoggerLevelConfig {

    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
