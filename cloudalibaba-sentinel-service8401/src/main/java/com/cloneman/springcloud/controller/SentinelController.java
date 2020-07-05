package com.cloneman.springcloud.controller;/*
 *#ClassName: SentinelController
 *#Author: lenovo
 *#Date: 2020/7/4 11:28
 */

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SentinelController {

    @GetMapping("/testA")
    public String testA(){
        return ">>>>testA";
    }

    @GetMapping("/testB")
    public String testB(){
        return ">>>>testB";
    }


    @GetMapping("/testC")
    public String testC(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ">>>>testC";
    }

    @GetMapping("/testD")
    public String testD(){
        int a=1/0;
        return ">>>>testD";
    }

    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey",blockHandler = "handle_HotKey")
    public String testHotKey(@RequestParam(required = false) String param1,
                        @RequestParam(required = false) String param2){
        return ">>>>testHotKey";
    }

    public String handle_HotKey(String p1, String p2, BlockException ex){
        return "服务异常，稍后再试";
    }
}
