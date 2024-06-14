package com.dime.ls.publisherevo.handler;

import com.dime.ls.publisherevo.model.Event;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.zeromq.ZMQ;

import java.util.UUID;
import java.util.random.RandomGenerator;

@Slf4j
@Configuration
public class EventPublisher {

    private static final RandomGenerator RANDOM = RandomGenerator.getDefault();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public CommandLineRunner publish() {
        return args -> {
            try (ZMQ.Context context = ZMQ.context(1); ZMQ.Socket publisher = context.socket(ZMQ.PUB)) {
                publisher.bind("tcp://*:5556");
                objectMapper.registerModule(new JavaTimeModule());
                while (!Thread.currentThread().isInterrupted()) {
                    Event event = Event.builder()
                            .data("Hello World!")
                            .key(UUID.randomUUID().toString())
                            .eventType(Event.Type.class.getEnumConstants()[RANDOM.nextInt(Event.Type.class.getEnumConstants().length)])
                            .build();
                    String stringEvent = objectMapper.writeValueAsString(event);
                    log.info("Publishing: {}", stringEvent);
                    publisher.send(stringEvent.getBytes());
                    Thread.sleep(1000);
                }
            }
        };
    }
}
