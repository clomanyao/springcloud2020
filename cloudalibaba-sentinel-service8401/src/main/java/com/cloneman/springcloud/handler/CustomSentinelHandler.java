package com.cloneman.springcloud.handler;
/*
 *#ClassName: CustomSentinelHandler
 *#Author: lenovo
 *#Date: 2020/7/5 11:07
 */

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.cloneman.springcloud.result.CommonResult;

public class CustomSentinelHandler {
    /*
    static 比加
    */
    public static CommonResult handler1(BlockException ex){
        return CommonResult.errorOf();
    }

    public static CommonResult handler2(BlockException ex){
        return CommonResult.errorOf();
    }
}
