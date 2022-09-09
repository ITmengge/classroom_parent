package com.simon.classroom.live.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mtcloud")            // 在配置文件中找到前缀是mtcloud的配置
public class MTCloudAccountConfig {

    private String openId;
    private String openToken;

}
