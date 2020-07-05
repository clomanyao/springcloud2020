package com.cloneman.springcloud.result;/*
 *#ClassName: CommonResult
 *#Author: lenovo
 *#Date: 2020/6/19 23:46
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommonResult <T>{

    private int code;
    private String msg;
    private T data;

    public CommonResult(int code,String msg){
        this(code,msg,null);
    }

    public static CommonResult errorOf(){
        CommonResult result = new CommonResult<>();
        result.setCode(500).setMsg("服务错误!").setData(null);
        return result;
    }

    public static CommonResult okOf(){
        CommonResult result = new CommonResult<>();
        result.setCode(200).setMsg("操作成功!").setData(null);
        return result;
    }

    public static CommonResult okOf(Object data){
        CommonResult result = new CommonResult<>();
        result.setCode(200).setMsg("操作成功!").setData(data);
        return result;
    }

}
