package com.cloneman.springcloud.filter;/*
 *#ClassName: MyLogGlobalFilter
 *#Author: lenovo
 *#Date: 2020/6/30 20:30
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//自定义全局过滤器,实现GlobalFilter, Ordered接口
@Component
public class MyLogGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyLogGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        LOGGER.info("欢迎进入gateway ");
        String username = exchange.getRequest().getQueryParams().getFirst("username");
        if (null == username) {
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);  //如果具有username 就把exchange 传入下一个过滤器
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
