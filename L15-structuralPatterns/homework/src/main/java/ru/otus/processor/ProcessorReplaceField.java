package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorReplaceField implements Processor {

    @Override
    public Message process(Message message) {
        // процессор, который поменяет местами значения field11 и field12

        var tempString = message.getField11();
        return message.toBuilder()
                .field11(message.getField12())
                .field12(tempString)
                .build();
    }
}
