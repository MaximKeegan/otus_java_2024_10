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
        //емае какой-то изврат. Но serialize->deserialize чот писать тоже не хотелось.

        ObjectForMessage field13Copy = new ObjectForMessage();
        List<String> dataCopy = new ArrayList<>(original.getField13().getData());
        field13Copy.setData(dataCopy);

        return new Message.Builder(original.getId())
                .field1(original.getField1())
                .field2(original.getField2())
                .field3(original.getField3())
                .field4(original.getField4())
                .field5(original.getField5())
                .field6(original.getField6())
                .field7(original.getField7())
                .field8(original.getField8())
                .field9(original.getField9())
                .field10(original.getField10())
                .field11(original.getField11())
                .field12(original.getField12())
                .field13(field13Copy)
                .build();
    }
}
