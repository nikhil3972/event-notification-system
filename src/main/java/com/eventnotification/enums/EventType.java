package com.eventnotification.enums;

public enum EventType {
    EMAIL(5), SMS(3), PUSH(2);

    private final int processingTimeSeconds;

    EventType(int processingTimeSeconds) {
        this.processingTimeSeconds = processingTimeSeconds;
    }

    public int getProcessingTimeSeconds() {
        return processingTimeSeconds;
    }
}
