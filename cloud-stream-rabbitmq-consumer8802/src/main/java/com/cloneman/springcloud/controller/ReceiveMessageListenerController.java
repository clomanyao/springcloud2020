package com.cloneman.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/*
 *#ClassName: ReceiveMessageListenerController
 *#Author: lenovo
 *#Date: 2020/7/1 16:31
 */
@Component
@EnableBinding(Sink.class)
public class ReceiveMessageListenerController {

    @Value("${server.port}")
    private String serverPort;

    /*
    * 发送时output.send(MessageBuilder.withPayload(message).build());message为String,
    * 所以这里用Message<String> 接受
    * */
    @StreamListener(Sink.INPUT)  //监听消息
    public void receive(Message<String> message){
        System.out.println("接受到的消息是："+message.getPayload()+"    port:"+serverPort);
    }
}
