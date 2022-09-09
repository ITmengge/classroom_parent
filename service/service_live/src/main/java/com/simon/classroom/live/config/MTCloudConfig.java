package com.simon.classroom.live.config;

import com.simon.classroom.live.mtcloud.MTCloud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MTCloudConfig {

    @Autowired
    private MTCloudAccountConfig mtCloudAccountConfig;

    // 注入MTCloud工具类
    @Bean
    public MTCloud mtCloudClient(){
        return new MTCloud(mtCloudAccountConfig.getOpenId(), mtCloudAccountConfig.getOpenToken());
    }
}
