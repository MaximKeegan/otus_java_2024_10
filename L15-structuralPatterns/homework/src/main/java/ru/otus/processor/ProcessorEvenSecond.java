package ru.otus.processor;

import ru.otus.model.Message;
import java.time.LocalDateTime;

public class ProcessorEvenSecond implements Processor{
    private final DateTimeProvider dateTimeProvider;

    public ProcessorEvenSecond(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message)  {
        LocalDateTime now = dateTimeProvider.getDate(); // Получаем текущее время
        int second = now.getSecond(); // Извлекаем секунду

        if (second % 2 == 0) {
            throw new IllegalStateException("Четная секунда");
        }

        return message;
    }
}
