package ru.otus.processor;

import java.time.LocalDateTime;

public class CurrentDateTimeProvider implements DateTimeProvider {
    @Override
    public LocalDateTime getDate() {
        return LocalDateTime.now();
    }
}