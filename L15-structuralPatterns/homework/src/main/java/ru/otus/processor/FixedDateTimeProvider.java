package ru.otus.processor;

import java.time.LocalDateTime;

public class FixedDateTimeProvider implements DateTimeProvider {
    private final LocalDateTime fixedTime;

    public FixedDateTimeProvider(LocalDateTime fixedTime) {
        this.fixedTime = fixedTime;
    }

    @Override
    public LocalDateTime getDate() {
        return fixedTime;
    }
}