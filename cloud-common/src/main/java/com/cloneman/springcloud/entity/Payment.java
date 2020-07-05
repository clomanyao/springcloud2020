package com.cloneman.springcloud.entity;/*
 *#ClassName: Parment
 *#Author: lenovo
 *#Date: 2020/6/27 18:02
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {
    private Long id;
    private String serial;
}
