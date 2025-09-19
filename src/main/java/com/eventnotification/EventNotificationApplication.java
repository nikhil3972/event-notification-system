package com.eventnotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EventNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventNotificationApplication.class, args);
	}

}
