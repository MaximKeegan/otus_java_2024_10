package ru.otus.numbers;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientStreamObserver implements StreamObserver<NumberResponse> {
    private static final Logger logger = LoggerFactory.getLogger(ClientStreamObserver.class);
    private volatile int lastValue;

    @Override
    public void onNext(NumberResponse numberResponse) {
        lastValue = numberResponse.getCurrentValue();
        logger.info("new value: {}", lastValue);
    }

    @Override
    public void onError(Throwable throwable) {
        logger.error("Error occurred: ", throwable);
    }

    @Override
    public void onCompleted() {
        logger.info("request completed");
    }

    public long getLastValue() {
        return lastValue;
    }
}
