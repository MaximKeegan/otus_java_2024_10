package ru.otus.numbers;

import io.grpc.ServerBuilder;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GRPCServer {
    private static final Logger logger = LoggerFactory.getLogger(GRPCServer.class);
    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        var server = ServerBuilder.forPort(SERVER_PORT)
                .addService(new NumbersServiceImpl())
                .build()
                .start();
        logger.info("server waiting for client connections...");
        server.awaitTermination();
    }
}
