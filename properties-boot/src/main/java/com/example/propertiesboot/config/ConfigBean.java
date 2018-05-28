package com.example.propertiesboot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "com.dudu")
@PropertySource("classpath:my.properties")
@Data
public class ConfigBean {
    private String name;
    private String want;

    // 省略getter和setter
}

