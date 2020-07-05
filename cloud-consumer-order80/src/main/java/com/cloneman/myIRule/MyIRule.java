package com.cloneman.myIRule;/*
 *#ClassName: MyIRule
 *#Author: lenovo
 *#Date: 2020/6/28 22:42
 */

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyIRule {

    @Bean
    public IRule mySelfIRule(){
        return new RandomRule();  //随机策略
    }
}
