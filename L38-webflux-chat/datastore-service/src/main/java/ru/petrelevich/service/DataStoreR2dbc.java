package ru.petrelevich.service;

import static java.time.temporal.ChronoUnit.MILLIS;
import static ru.petrelevich.config.ApplConfig.SPECIAL_ROOM_ID;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Scheduler;
import ru.petrelevich.domain.Message;
import ru.petrelevich.repository.MessageRepository;

@Service
public class DataStoreR2dbc implements DataStore {
    private static final Logger log = LoggerFactory.getLogger(DataStoreR2dbc.class);
    private final MessageRepository messageRepository;
    private final Scheduler workerPool;
    private final Sinks.Many<Message> messageSink;

    public DataStoreR2dbc(Scheduler workerPool, MessageRepository messageRepository) {
        this.workerPool = workerPool;
        this.messageRepository = messageRepository;
        this.messageSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @Override
    public Mono<Message> saveMessage(Message message) {
        log.info("saveMessage:{}", message);
        return messageRepository.save(message).doOnNext(saved -> {
            log.info("Publishing message to sink:{}", saved);
            messageSink.tryEmitNext(saved);
        });
    }

    @Override
    public Flux<Message> loadMessages(String roomId) {
        log.info("loadMessages roomId:{}", roomId);
        Flux<Message> historicalMessages = roomId.equals(SPECIAL_ROOM_ID)
                ? messageRepository.findAllOrderedById()
                : messageRepository.findByRoomId(roomId);

        Flux<Message> liveMessages = messageSink
                .asFlux()
                .filter(message -> roomId.equals(SPECIAL_ROOM_ID)
                        ? !message.roomId().equals(SPECIAL_ROOM_ID)
                        : message.roomId().equals(roomId));

        return Flux.merge(historicalMessages, liveMessages).delayElements(Duration.of(100, MILLIS), workerPool);
    }
}
