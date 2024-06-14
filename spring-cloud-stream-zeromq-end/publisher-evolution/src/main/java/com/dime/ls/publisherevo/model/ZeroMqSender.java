package com.dime.ls.publisherevo.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.UUID;
import java.util.random.RandomGenerator;

@Component
public class ZeroMqSender implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(ZeroMqSender.class);

    private static final RandomGenerator RANDOM = RandomGenerator.getDefault();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Async("taskExecutor")
    @Override
    public void run(String... args) throws Exception {
        try (ZContext context = new ZContext()) {
            ZMQ.Socket socket = context.createSocket(SocketType.PUSH);
            socket.bind("tcp://*:5555");

            objectMapper.registerModule(new JavaTimeModule());

            while (!Thread.currentThread().isInterrupted()) {
                Event event = Event.builder()
                        .data("Hello World!")
                        .key(UUID.randomUUID().toString())
                        .eventType(Event.Type.class.getEnumConstants()[RANDOM.nextInt(Event.Type.class.getEnumConstants().length)])
                        .build();
                String stringEvent = objectMapper.writeValueAsString(event);
                LOG.info("Sending: {} --> {}", event.getEventType(), event);
                socket.send(stringEvent.getBytes(), ZMQ.DONTWAIT);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
