package ru.otus.numbers;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GRPCClient {
    private static final Logger logger = LoggerFactory.getLogger(GRPCClient.class);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) {
        logger.info("Starting client");
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var stub = NumbersServiceGrpc.newStub(channel);

        var streamObserver = new ClientStreamObserver();

        NumberRequest request =
                NumberRequest.newBuilder().setFirstValue(0).setLastValue(30).build();

        stub.getNumbers(request, streamObserver);

        long currentValue = 0;
        long lastProcessedValue = 0;

        for (int i = 0; i <= 50; i++) {
            try {
                long serverValue = streamObserver.getLastValue();
                if (serverValue != lastProcessedValue && serverValue != 0) {
                    currentValue += serverValue + 1;
                    lastProcessedValue = serverValue;
                } else {
                    currentValue += 1;
                }
                logger.info("currentValue: {}", currentValue);
                Thread.sleep(1000); // 1 second delay
            } catch (InterruptedException e) {
                logger.error("Interrupted while running", e);
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
