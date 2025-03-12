package ru.otus.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPong {
    private static final Logger logger = LoggerFactory.getLogger(PingPong.class);
    private String last = "PONG";
    private int counter = 1;
    private int repeatCount = 0;
    private int direction = 1;

    private synchronized void action(String message) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // spurious wakeup https://en.wikipedia.org/wiki/Spurious_wakeup
                // поэтому не if
                while (last.equals(message)) {
                    this.wait();
                }

                logger.info(String.valueOf(counter));
                repeatCount++;

                if (repeatCount == 2) {
                    counter += direction;
                    repeatCount = 0;
                    if (counter >= 10) direction = -1;
                    if (counter <= 1) direction = 1;
                }

                last = message;
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        PingPong pingPong = new PingPong();
        new Thread(() -> pingPong.action("ping")).start();
        new Thread(() -> pingPong.action("PONG")).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
