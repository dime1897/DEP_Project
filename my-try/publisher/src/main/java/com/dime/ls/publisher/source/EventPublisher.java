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
    public CommandLineRunner publish() {
        return args -> {
            try (ZMQ.Context context = ZMQ.context(1); ZMQ.Socket publisher = context.socket(SocketType.DEALER)) {
                publisher.setIdentity(String.format("%04X-%04X", RANDOM.nextInt(), RANDOM.nextInt()).getBytes(ZMQ.CHARSET));
                publisher.connect("tcp://subscriber:5555");

                objectMapper.registerModule(new JavaTimeModule());
                while (!Thread.currentThread().isInterrupted()) {
                    Event event = Event.builder()
                            .data("Hello World!")
                            .key(UUID.randomUUID().toString())
                            .eventType(Event.Type.class.getEnumConstants()[RANDOM.nextInt(Event.Type.class.getEnumConstants().length)])
                            .build();
                    String stringEvent = objectMapper.writeValueAsString(event);

                    publisher.send(stringEvent.getBytes());
                    log.info("Sent: {}", stringEvent);
                    Thread.sleep(1000);
                }
            }
        };
    }
}
