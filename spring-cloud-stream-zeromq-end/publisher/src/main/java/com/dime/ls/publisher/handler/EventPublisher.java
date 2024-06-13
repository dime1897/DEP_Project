package com.dime.ls.publisher.handler;

import com.dime.ls.publisher.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zeromq.ZMQ;

import java.io.ObjectOutputStream;
import java.util.UUID;
import java.util.random.RandomGenerator;

@Slf4j
@Configuration
public class EventPublisher {

    private static final RandomGenerator RANDOM = RandomGenerator.getDefault();

    @Bean
    public CommandLineRunner publish() {
        return args -> {
            try (ZMQ.Context context = ZMQ.context(1); ZMQ.Socket publisher = context.socket(ZMQ.PUB)) {
                publisher.bind("tcp://*:5556");
                while (!Thread.currentThread().isInterrupted()) {
                    Event message = Event.builder()
                            .data("Hello World!")
                            .key(UUID.randomUUID().toString())
                            .eventType(Event.Type.class.getEnumConstants()[RANDOM.nextInt(Event.Type.class.getEnumConstants().length)])
                            .build();
                    log.info("Publishing: {}", message);
                    publisher.send(message.toString());
                    Thread.sleep(1000);
                }
            }
        };
    }
}
