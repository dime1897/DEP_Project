package com.dime.ls.producer.source;

import com.dime.ls.producer.model.Event;
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
public class EventProducer {

    private static final RandomGenerator RANDOM = RandomGenerator.getDefault();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public CommandLineRunner send() {
        return args -> {
            try (ZMQ.Context context = ZMQ.context(1); ZMQ.Socket producer = context.socket(SocketType.DEALER)) {
                producer.setIdentity("PRODUCER-0".getBytes(ZMQ.CHARSET));
                producer.connect("tcp://consumer-1:5555");
                producer.connect("tcp://consumer-0:5555");

                objectMapper.registerModule(new JavaTimeModule());
                while (!Thread.currentThread().isInterrupted()) {
                    Event event = Event.builder()
                            .data("Hello World!")
                            .key(UUID.randomUUID().toString())
                            .eventType(Event.Type.class.getEnumConstants()[RANDOM.nextInt(Event.Type.class.getEnumConstants().length)])
                            .build();
                    String stringEvent = objectMapper.writeValueAsString(event);

                    producer.send(stringEvent.getBytes());
                    log.info("Sent: {}", stringEvent);
                    Thread.sleep(1000);
                }
            }
        };
    }
}
