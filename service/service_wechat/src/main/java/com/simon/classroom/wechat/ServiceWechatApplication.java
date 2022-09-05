package com.simon.classroom.wechat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.simon")     // 开启远程调用
@MapperScan("com.simon.classroom.wechat.mapper")    // 扫描mapper文件
@ComponentScan(basePackages = "com.simon")          // 扫描配置类
public class ServiceWechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceWechatApplication.class, args);
    }
}
