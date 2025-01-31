package ru.otus.listener.homework;

import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> messageHistory = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        Message copy = deepCopyMessage(msg);
        messageHistory.put(copy.getId(), copy);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messageHistory.get(id)).map(this::deepCopyMessage);
    }

    private Message deepCopyMessage(Message original) {
        var copy = original.toBuilder();
        if (original.getField13() != null && original.getField13().getData() != null) {
            ObjectForMessage field13Copy = new ObjectForMessage();
            List<String> dataCopy = new ArrayList<>(original.getField13().getData());
            field13Copy.setData(dataCopy);
            copy.field13(field13Copy);
        }

        return copy.build();
    }
}
