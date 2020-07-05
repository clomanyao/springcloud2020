package com.cloneman.springcloud.lb;/*
 *#ClassName: MyLoadBalanced
 *#Author: lenovo
 *#Date: 2020/6/29 10:36
 */

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MyLoadBalanced implements LoadBalanced{

    private AtomicInteger atomicInteger=new AtomicInteger(0);  //初始值为0

    /*
    * 这里采用CAS自旋锁来代替synchronized等关键字
    * ① 这里采用CAS自旋锁 和 synchronized 关键字一样,是为了防止当有多个线程访问该方法的时候能保持方法的原子性,不会影响算法的最终结果
    * ② 采用synchronized对CPU功能消耗比较高,这里采用CAS自旋锁更为合适.
    * */
    private final int getServerIndex(){
        for (;;){
            int current=atomicInteger.get();
            int next=current>Integer.MAX_VALUE?0:current+1;
            /*
            * compareAndSet:
            * 当初始值(AtomicInteger atomicInteger=new AtomicInteger(0))与实际值(current)一样
            * 就会把next替换为atomicInteger.get()的值
            * */
            if(atomicInteger.compareAndSet(current,next)){
                return next;
            }
        }
    }

    @Override
    public ServiceInstance getInstance(List<ServiceInstance> instances) {
        int index = getServerIndex();
        if(instances.size()==0||instances==null){
            return null;
        }else {
            int i=index%instances.size();
            return instances.get(i);
        }
    }

}
