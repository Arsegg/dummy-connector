package com.github.arsegg.homework2;

import ru.pflb.mq.dummy.exception.DummyException;
import ru.pflb.mq.dummy.implementation.ConnectionImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws DummyException, IOException, InterruptedException {
        final var first = args.length > 0 ? args[0] : "messages.dat";
        try (final var connection = new ConnectionImpl();
             final var session = connection.createSession(true)) {
            final var myQueue = session.createDestination("myQueue");
            final var producer = session.createProducer(myQueue);
            while (true) {
                // TODO: how to remove infinite creation of Files.newBufferedReader?
                // 1) Create br once, br.mark(Integer.MAX_VALUE), br.readAll(), br.reset(); Cons: memory;
                // 2) Create RandomAccessFile once, raf.readAll(), raf.seek(0); Cons: encoding and performance;
                // 3) Do nothing... Pros: O(1) memory complexity (depends on length of lines);
                // 4) Read content of file into memory. Cons: memory, same as 1.
                try (final var bufferedReader = Files.newBufferedReader(Path.of(first))) {
                    String message;
                    while ((message = bufferedReader.readLine()) != null) {
                        producer.send(message);
                        Thread.sleep(2000);
                    }
                }
            }
        }
    }
}
