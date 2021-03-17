package com.github.arsegg.homework1;

import ru.pflb.mq.dummy.exception.DummyException;
import ru.pflb.mq.dummy.implementation.ConnectionImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) throws DummyException, InterruptedException {
        try (final var connection = new ConnectionImpl();
             final var session = connection.createSession(true)) {
            final var myQueue = session.createDestination("myQueue");
            final var producer = session.createProducer(myQueue);
            final var messages = List.of("Раз", "Два", "Три");
            for (String message : messages) {
                producer.send(message);
                Thread.sleep(2000);
            }
        }
    }
}
