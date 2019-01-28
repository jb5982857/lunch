package com.lunch.ip;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//用mybatis时必须要在这里设置dao函数的父路径
@MapperScan("com.lunch.ip.dao")
//用redis时必须要在这里配置
@EnableScheduling
//如果bean在其他路径模块的时候，默认是不会被扫描到的，需要这里添加下路径
@ComponentScan(basePackages = {"com.lunch.*"})
@EnableCaching
@EnableEurekaClient
public class IpApplication {
    public static void main(String[] args) {
        SpringApplication.run(IpApplication.class, args);
    }
}
