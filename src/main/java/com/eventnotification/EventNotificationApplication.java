package com.eventnotification;

import com.eventnotification.shutdown.GracefulShutdownManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EventNotificationApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(EventNotificationApplication.class, args);

		// Setup graceful shutdown
		GracefulShutdownManager shutdownManager = context.getBean(GracefulShutdownManager.class);
		Runtime.getRuntime().addShutdownHook(new Thread(shutdownManager::shutdown));
	}

}
