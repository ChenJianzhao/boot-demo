package com.example.logbackboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
//		(exclude = {
//		DataSourceAutoConfiguration.class,
//		DruidDataSourceAutoConfigure.class
//})
public class LogbackBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogbackBootApplication.class, args);
	}
}
