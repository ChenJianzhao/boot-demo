package com.example.scheduleboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScheduleBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScheduleBootApplication.class, args);
	}
}
