package com.cloneman.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.cloneman.springcloud.entity.Payment;
import com.cloneman.springcloud.handler.CustomSentinelHandler;
import com.cloneman.springcloud.result.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 *#ClassName: RateLimitController
 *#Author: lenovo
 *#Date: 2020/7/5 10:46
 */
@RestController
public class RateLimitController {

    @GetMapping("/byResource")
    @SentinelResource(value = "byResource", blockHandler = "handle_byResource")
    public CommonResult byResource() {
        return CommonResult.okOf();
    }

    public CommonResult handle_byResource(BlockException ex) {
        return CommonResult.errorOf();
    }

    @GetMapping("/rateLimit/byUrl")
    @SentinelResource(value = "byUrl")
    public CommonResult byUrl() {
        return new CommonResult(200, "按url限流测试OK", new Payment(2020L, "serial002"));
    }


    @GetMapping("/customhandler")
    @SentinelResource(value = "customhandler",
            blockHandlerClass = CustomSentinelHandler.class,
            blockHandler = "handler1")
    public CommonResult customhandler() {
        return CommonResult.okOf();
    }
}
