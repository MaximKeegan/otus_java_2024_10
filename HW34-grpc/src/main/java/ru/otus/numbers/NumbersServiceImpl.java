package ru.otus.numbers;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumbersServiceImpl extends NumbersServiceGrpc.NumbersServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(NumbersServiceImpl.class);

    @Override
    public void getNumbers(NumberRequest request, StreamObserver<NumberResponse> responseObserver) {
        logger.info("Received request: firstValue={}, lastValue={}", request.getFirstValue(), request.getLastValue());

        try {
            for (int i = request.getFirstValue() + 1; i <= request.getLastValue(); i++) {
                NumberResponse response =
                        NumberResponse.newBuilder().setCurrentValue(i).build();
                responseObserver.onNext(response);
                logger.info("Sent value: {}", i);
                Thread.sleep(2000); // 2 seconds delay
            }
        } catch (InterruptedException e) {
            logger.error("Interrupted while streaming", e);
            Thread.currentThread().interrupt();
        }

        responseObserver.onCompleted();
        logger.info("Stream completed");
    }
}
