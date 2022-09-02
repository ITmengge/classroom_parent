package com.simon.classroom.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient      // 在nacos注册中心进行注册
@ComponentScan(basePackages = "com.simon")  // 扫描所有com.simon开头的包，包括扫描service_utils下的Swagger2Config配置类
public class ServiceVodApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceVodApplication.class, args);
    }
}
