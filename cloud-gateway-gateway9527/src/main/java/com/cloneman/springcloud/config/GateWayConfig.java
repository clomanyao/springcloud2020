package com.cloneman.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 *#ClassName: GateWayConfig
 *#Author: lenovo
 *#Date: 2020/6/30 19:17
 */
@Configuration
public class GateWayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        RouteLocator build = routes.route("payment_route03", r -> r.path("/guonei").uri("http://news.baidu.com")).build();
        return build;
    }
}
