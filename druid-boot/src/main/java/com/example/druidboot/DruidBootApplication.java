package com.example.druidboot;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * MyBatis-Spring-Boot-Starter依赖将会提供如下：
 *
 * 自动检测现有的DataSource
 * 将创建并注册SqlSessionFactory的实例，该实例使用SqlSessionFactoryBean将该DataSource作为输入进行传递
 * 将创建并注册从SqlSessionFactory中获取的SqlSessionTemplate的实例。
 * 自动扫描您的mappers，将它们链接到SqlSessionTemplate并将其注册到Spring上下文，以便将它们注入到您的bean中。
 */

@SpringBootApplication
@EnableAspectJAutoProxy
//@EnableConfigurationProperties(DruidConfiguration.class)
public class DruidBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(DruidBootApplication.class, args);
    }
}
