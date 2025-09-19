package com.eventnotification.shutdown;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Component
public class GracefulShutdownManager {

    private static final Logger logger = LoggerFactory.getLogger(GracefulShutdownManager.class);

    @Autowired
    private Executor emailExecutor;

    @Autowired
    private Executor smsExecutor;

    @Autowired
    private Executor pushExecutor;

    public void shutdown() {
        logger.info("Initiating graceful shutdown...");

        // Shutdown all executors
        shutdownExecutor("EmailExecutor", emailExecutor);
        shutdownExecutor("SmsExecutor", smsExecutor);
        shutdownExecutor("PushExecutor", pushExecutor);

        logger.info("Graceful shutdown completed");
    }

    private void shutdownExecutor(String name, Executor executor) {
        if (!(executor instanceof ThreadPoolTaskExecutor tpExecutor)) {
            logger.warn("{} is not a ThreadPoolTaskExecutor, skipping shutdown", name);
            return;
        }

        try {
            logger.info("Shutting down {}", name);
            tpExecutor.shutdown();

            // Wait for tasks to complete
            boolean terminated = tpExecutor.getThreadPoolExecutor()
                    .awaitTermination(60, TimeUnit.SECONDS);

            if (!terminated) {
                logger.warn("{} did not terminate within timeout, forcing shutdown", name);
                tpExecutor.getThreadPoolExecutor().shutdownNow();
            } else {
                logger.info("{} shutdown completed", name);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted while shutting down {}", name);
        }
    }
}