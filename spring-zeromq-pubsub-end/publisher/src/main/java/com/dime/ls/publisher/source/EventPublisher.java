package com.dime.ls.publisher.source;

import com.dime.ls.publisher.model.Event;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.UUID;
import java.util.random.RandomGenerator;

@Slf4j
@Configuration
public class EventPublisher {

    private static final RandomGenerator RANDOM = RandomGenerator.getDefault();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public CommandLineRunner send() {
        return args -> {
            try (ZMQ.Context context = ZMQ.context(1); ZMQ.Socket publisher = context.socket(SocketType.PUB)) {
                publisher.bind("tcp://*:5555");
                Thread.sleep(8000); //waiting for consumer connecting (necessary)
                objectMapper.registerModule(new JavaTimeModule());
                while (!Thread.currentThread().isInterrupted()) {
                    Event event = Event.builder()
                            .data("Hello World!")
                            .key(UUID.randomUUID().toString())
                            .eventType(Event.Type.class.getEnumConstants()[RANDOM.nextInt(Event.Type.class.getEnumConstants().length)])
                            .build();
                    String stringEvent = objectMapper.writeValueAsString(event);

                    String topic = "topic-" + RANDOM.nextInt(0,3);
                    log.info("Choosen topic: {}", topic);
                    publisher.sendMore(topic); //Randomly choose the topic
                    publisher.send(stringEvent.getBytes(), ZMQ.DONTWAIT);
                    log.info("Sent: {}", stringEvent);
                    Thread.sleep(1000);
                }
            }
        };
    }
}
